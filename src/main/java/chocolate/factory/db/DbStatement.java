package chocolate.factory.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DbStatement implements AutoCloseable {

    private final PreparedStatement statement;

    public DbStatement(PreparedStatement statement) {
        this.statement = statement;
    }

    public void setStatementParameters(Collection<Object> values) throws SQLException {
        int index = 1;
        for (Object value : values) {
            statement.setObject(index++, value);
        }
    }

    public void setParam(int index, Object value, int sqlType) throws SQLException {
        statement.setObject(index, value, sqlType);
    }

    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }

    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }

    public Optional<List<Object>> getGeneratedFields() throws SQLException {
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            ResultSetMetaData metaData = generatedKeys.getMetaData();
            if (generatedKeys.next()) {
                List<Object> values = new ArrayList<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Object value = generatedKeys.getObject(i);
                    values.add(value);
                }

                return Optional.of(values);
            } else {
                return Optional.empty();
            }
        }
    }

    @Override
    public void close() throws Exception {
        statement.close();
    }
}
