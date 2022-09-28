package chocolate.factory.db;

import chocolate.factory.db.call.ReflectiveExecutor;
import chocolate.factory.db.dao.Dao;
import chocolate.factory.db.dao.ReflectiveModelDao;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection implements AutoCloseable {

    private final Connection connection;
    private final ReflectiveExecutor executor;

    public DbConnection(Connection connection) {
        this.connection = connection;
        executor = new ReflectiveExecutor(connection);
    }

    public DbStatement newStatement(String format) throws SQLException {
        return new DbStatement(connection.prepareStatement(format));
    }

    public <T> void executeProcedure(T procedure) throws SQLException {
        executor.executeProcedure(procedure);
    }

    public <T> Dao<T> newDao(Class<T> type) {
        return new ReflectiveModelDao<>(this, type);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
