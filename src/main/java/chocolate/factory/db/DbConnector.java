package chocolate.factory.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DbConnector {

    private final Properties connectionProperties;
    private final Lock connectionLock;
    private final Collection<ConnectionContext> contexts;

    public DbConnector(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
        connectionLock = new ReentrantLock();
        contexts = new HashSet<>();

        Thread cleanupThread = new Thread(this::cleanUnusedConnection, "connection-cleanup");
        cleanupThread.setDaemon(true);
        cleanupThread.start();
    }

    public DbConnection getConnection() {
        connectionLock.lock();
        try {
            for (ConnectionContext connectionContext : contexts) {
                try {
                    if (connectionContext.isUsed || connectionContext.markedForClose) {
                        continue;
                    }
                    if (connectionContext.connection.isClosed()) {
                        continue;
                    }

                    connectionContext.isUsed = true;
                    connectionContext.lastUseTimestamp = System.nanoTime();
                    return new DbConnection(connectionContext.connection);
                } catch (SQLException e) {
                    connectionContext.markedForClose = true;
                }
            }

            ConnectionContext connectionContext = openConnection();
            contexts.add(connectionContext);

            connectionContext.isUsed = true;
            connectionContext.lastUseTimestamp = System.nanoTime();
            return new DbConnection(connectionContext.connection);
        } finally {
            connectionLock.unlock();
        }
    }

    private void cleanUnusedConnection() {
        final long CACHED_TIMEOUT_NANOS = 5L * 60L * 1000000000L; // 5 min

        connectionLock.lock();
        try {
            for (Iterator<ConnectionContext> iterator = contexts.iterator(); iterator.hasNext();) {
                ConnectionContext connectionContext = iterator.next();
                try {
                    if (connectionContext.isUsed) {
                        continue;
                    }
                    if (connectionContext.connection.isClosed()) {
                        iterator.remove();
                    }
                    if (connectionContext.markedForClose ||
                            System.nanoTime() - connectionContext.lastUseTimestamp >= CACHED_TIMEOUT_NANOS) {
                        connectionContext.connection.close();
                        iterator.remove();
                    }


                } catch (SQLException e) {
                    connectionContext.markedForClose = true;
                    try {
                        connectionContext.connection.close();
                    } catch (SQLException ex) {
                        // bad
                        ex.printStackTrace();
                    }
                }
            }
        } finally {
            connectionLock.unlock();
        }
    }

    private ConnectionContext openConnection() {
        try {
            Class.forName(connectionProperties.getProperty("db.driver"));
            Connection connection = DriverManager.getConnection(connectionProperties.getProperty("db.url"));
            return new ConnectionContext(connection);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Error(e);
        }
    }

    private static class ConnectionContext {
        Connection connection;
        boolean isUsed;
        long lastUseTimestamp;
        boolean markedForClose;

        ConnectionContext(Connection connection) {
            this.connection = connection;
            isUsed = false;
            lastUseTimestamp = -1;
            markedForClose = false;
        }
    }
}
