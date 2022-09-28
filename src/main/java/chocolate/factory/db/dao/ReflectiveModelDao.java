package chocolate.factory.db.dao;

import chocolate.factory.db.DbConnection;
import chocolate.factory.db.DbStatement;
import chocolate.factory.db.QueryBuilder;
import chocolate.factory.db.TypeAdapter;
import chocolate.factory.util.reflect.Reflection;
import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReflectiveModelDao<T> implements Dao<T> {

    private final DbConnection connection;
    private final Class<T> type;

    public ReflectiveModelDao(DbConnection connection, Class<T> type) {
        this.connection = connection;
        this.type = type;
    }

    @Override
    public List<T> getAll() throws DaoException {
        Table table = type.getAnnotation(Table.class);
        String tableName = table.value();
        Set<? extends Field> fields = Reflection.getFieldsWithAnnotation(type, Column.class);

        String sql;
        if (table.isView()) {
            sql = QueryBuilder.buildSelect(tableName, null);
        } else {
            Set<String> fieldNames = fields.stream()
                    .map((f)-> {
                        Column column = f.getAnnotation(Column.class);
                        return column.value();
                    })
                    .collect(Collectors.toSet());
            sql = QueryBuilder.buildSelect(tableName, fieldNames);
        }

        try (DbStatement statement = connection.newStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<T> result = new ArrayList<>();

                while (resultSet.next()) {
                    result.add(readRow(resultSet));
                }

                return result;
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<T> findByColumnValue(String columnName, Object value) throws DaoException {
        Table table = type.getAnnotation(Table.class);
        String tableName = table.value();
        Set<? extends Field> fields = Reflection.getFieldsWithAnnotation(type, Column.class);

        String sql;
        if (table.isView()) {
            sql = QueryBuilder.buildSelectWithWhereEqual(tableName, null, columnName);
        } else {
            Set<String> fieldNames = fields.stream()
                    .map((f)-> {
                        Column column = f.getAnnotation(Column.class);
                        return column.value();
                    })
                    .collect(Collectors.toSet());
            sql = QueryBuilder.buildSelectWithWhereEqual(tableName, fieldNames, columnName);
        }

        try (DbStatement statement = connection.newStatement(sql)) {
            statement.setStatementParameters(Collections.singleton(value));
            try (ResultSet resultSet = statement.executeQuery()) {
                List<T> result = new ArrayList<>();

                while (resultSet.next()) {
                    result.add(readRow(resultSet));
                }

                return result;
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void insert(T obj) throws DaoException {
        Table table = type.getAnnotation(Table.class);
        if (table.isView()) {
            throw new DaoException("cannot insert into view");
        }

        String tableName = table.value();
        List<? extends Field> fields =
                new ArrayList<>(Reflection.getFieldsWithAnnotationAndValues(type, Column.class, Predicate.not(Column::primaryKey)));
        List<String> fieldNames = fields.stream()
                .map((f)-> {
                    Column column = f.getAnnotation(Column.class);
                    return column.value();
                })
                .collect(Collectors.toList());

        String sql = QueryBuilder.buildInsert(tableName, fieldNames);
        try (DbStatement statement = connection.newStatement(sql)) {
            int index = 1;
            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                field.setAccessible(true);
                Object value = field.get(obj);
                value = TypeAdapter.objectToSql(field, value);

                statement.setParam(index++, value, column.sqlType());
            }

            int result = statement.executeUpdate();
            if (result != 1) {
                throw new SQLException("insert statement returned " + result);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(T obj) throws DaoException {
        try {
            Table table = type.getAnnotation(Table.class);
            if (table.isView()) {
                throw new DaoException("cannot update into view");
            }

            String tableName = table.value();
            List<? extends Field> fields =
                    new ArrayList<>(Reflection.getFieldsWithAnnotationAndValues(type, Column.class, Predicate.not(Column::primaryKey)));
            List<String> fieldNames = fields.stream()
                    .map((f)-> {
                        Column column = f.getAnnotation(Column.class);
                        return column.value();
                    })
                    .collect(Collectors.toList());
            Pair<Column, Object> primaryKey = findPrimaryKey(obj);

            String sql = QueryBuilder.buildUpdate(tableName, fieldNames, primaryKey.getKey().value());
            try (DbStatement statement = connection.newStatement(sql)) {
                int index = 1;
                for (Field field : fields) {
                    Column column = field.getAnnotation(Column.class);
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    value = TypeAdapter.objectToSql(field, value);

                    statement.setParam(index++, value, column.sqlType());
                }
                statement.setParam(index, primaryKey.getValue(), primaryKey.getKey().sqlType());

                int result = statement.executeUpdate();
                if (result != 1) {
                    throw new SQLException("update statement returned " + result);
                }
            } catch (Exception e) {
                throw new DaoException(e);
            }
        } catch (IllegalAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(T obj) throws DaoException {
        try {
            Table table = type.getAnnotation(Table.class);
            if (table.isView()) {
                throw new DaoException("cannot delete into view");
            }

            String tableName = table.value();
            Pair<Column, Object> primaryKey = findPrimaryKey(obj);

            String sql = QueryBuilder.buildDelete(tableName, primaryKey.getKey().value());
            try (DbStatement statement = connection.newStatement(sql)) {
                statement.setParam(1, primaryKey.getValue(), primaryKey.getKey().sqlType());
                int result = statement.executeUpdate();
                if (result != 1) {
                    throw new SQLException("delete statement returned " + result);
                }
            } catch (Exception e) {
                throw new DaoException(e);
            }
        } catch (IllegalAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteByColumnValue(String columnName, Object value) throws DaoException {
        Table table = type.getAnnotation(Table.class);
        if (table.isView()) {
            throw new DaoException("cannot delete into view");
        }

        String tableName = table.value();

        String sql = QueryBuilder.buildDelete(tableName, columnName);
        try (DbStatement statement = connection.newStatement(sql)) {
            statement.setStatementParameters(Collections.singleton(value));
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new SQLException("delete statement returned " + result);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private Pair<Column, Object> findPrimaryKey(T t) throws DaoException, IllegalAccessException {
        Set<? extends Field> fields =
                Reflection.getFieldsWithAnnotationAndValues(type, Column.class, Column::primaryKey);
        if (fields.isEmpty()) {
            throw new DaoException("requires primary key");
        }

        Field field = fields.iterator().next();
        field.setAccessible(true);
        Object value = field.get(t);
        value = TypeAdapter.objectToSql(field, value);

        Column column = field.getAnnotation(Column.class);
        return new Pair<>(column, value);
    }

    private Map<String, Object> collectFieldValues(Set<? extends Field> fields, Object instance)
            throws IllegalAccessException {
        Map<String, Object> values = new HashMap<>();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);

            field.setAccessible(true);
            Object value = field.get(instance);
            value = TypeAdapter.objectToSql(field, value);

            values.put(column.value(), value);
        }

        return values;
    }

    private T readRow(ResultSet resultSet)
            throws SQLException, IllegalAccessException, InvocationTargetException,
            InstantiationException, NoSuchMethodException {
        Constructor<T> ctor = type.getConstructor();
        T instance = ctor.newInstance();

        Set<? extends Field> fields = Reflection.getFieldsWithAnnotation(type, Column.class);
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            Object value = resultSet.getObject(column.value(), TypeAdapter.adaptTypeToSql(field.getType()));
            value = TypeAdapter.sqlToObject(field, value);

            field.setAccessible(true);
            field.set(instance, value);
        }

        return instance;
    }
}
