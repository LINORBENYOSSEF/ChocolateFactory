package chocolate.factory.db;

public class ExpectedOneResultException extends DbOpsException {

    public ExpectedOneResultException(int actualAmount) {
        super(String.format("Expected one, got %d", actualAmount));
    }
}
