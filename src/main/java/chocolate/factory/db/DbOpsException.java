package chocolate.factory.db;

public class DbOpsException extends Exception {

    public DbOpsException(String message) {
        super(message);
    }

    public DbOpsException(Throwable cause) {
        super(cause);
    }
}
