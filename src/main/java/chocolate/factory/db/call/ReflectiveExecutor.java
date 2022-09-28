package chocolate.factory.db.call;

import chocolate.factory.db.QueryBuilder;
import chocolate.factory.db.TypeAdapter;
import chocolate.factory.util.reflect.Reflection;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectiveExecutor {

    private final Connection connection;

    public ReflectiveExecutor(Connection connection) {
        this.connection = connection;
    }

    public <T> void executeProcedure(T procedure) throws SQLException {
        StoredProcedure storedProcedure = procedure.getClass().getAnnotation(StoredProcedure.class);
        if (storedProcedure == null) {
            throw new IllegalArgumentException("expected type with StoredProcedure annotation");
        }

        Set<? extends Field> parameterFields = Reflection.getFieldsWithAnnotation(
                procedure.getClass(), Parameter.class);
        List<? extends Field> inParams = parameterFields.stream().filter((f)-> {
            Parameter parameter = f.getAnnotation(Parameter.class);
            return !parameter.outParam();
        }).sorted(Comparator.comparingInt((f)-> {
            Parameter parameter = f.getAnnotation(Parameter.class);
            return parameter.index();
        })).collect(Collectors.toList());
        List<? extends Field> outParams = parameterFields.stream().filter((f)-> {
            Parameter parameter = f.getAnnotation(Parameter.class);
            return parameter.outParam();
        }).sorted(Comparator.comparingInt((f)-> {
            Parameter parameter = f.getAnnotation(Parameter.class);
            return parameter.index();
        })).collect(Collectors.toList());

        try {
            String sql = QueryBuilder.buildProcedureCall(storedProcedure.value(), inParams.size() + outParams.size());
            try (CallableStatement statement = connection.prepareCall(sql)) {
                for (Field field : inParams) {
                    Parameter parameter = field.getAnnotation(Parameter.class);
                    field.setAccessible(true);
                    Object value = field.get(procedure);
                    value = TypeAdapter.objectToSql(field, value);
                    statement.setObject(parameter.index(), value);
                }
                for (Field field : outParams) {
                    Parameter parameter = field.getAnnotation(Parameter.class);
                    statement.registerOutParameter(parameter.index(), parameter.sqlType());
                }

                statement.executeUpdate();

                for (Field field : outParams) {
                    Parameter parameter = field.getAnnotation(Parameter.class);
                    Object value = statement.getObject(parameter.index());
                    value = TypeAdapter.sqlToObject(field, value);
                    field.setAccessible(true);
                    field.set(procedure, value);
                }
            }
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        }
    }
}
