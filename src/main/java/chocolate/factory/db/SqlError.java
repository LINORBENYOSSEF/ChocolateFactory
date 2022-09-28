package chocolate.factory.db;

import java.util.Optional;

public enum SqlError {
    BAD_ARGUMENT(-100000) {
        @Override
        public DbOpsException newException() {
            return new DbOpsException("BAD_ARGUMENT");
        }
    },
    MISSING_ARGUMENT(100001) {
        @Override
        public DbOpsException newException() {
            return new DbOpsException("MISSING_ARGUMENT");
        }
    },
    NO_SUCH_ROW(100002) {
        @Override
        public DbOpsException newException() {
            return new DbOpsException("NO_SUCH_ROW");
        }
    },
    NOT_ENOUGH_TO_FULFILL(100003) {
        @Override
        public DbOpsException newException() {
            return new DbOpsException("NOT_ENOUGH_TO_FULFILL");
        }
    },
    ORDER_ALREADY_COMPLETE(100004) {
        @Override
        public DbOpsException newException() {
            return new DbOpsException("ORDER_ALREADY_COMPLETE");
        }
    },
    BAD_EMPLOYEE_POSITION(100005) {
        @Override
        public DbOpsException newException() {
            return new DbOpsException("BAD_EMPLOYEE_POSITION");
        }
    }
    ;
    private final int code;

    SqlError(int code) {
        this.code = code;
    }

    public abstract DbOpsException newException();

    public static Optional<SqlError> getForCode(int code) {
        for (SqlError error : values()) {
            if (error.code == code) {
                return Optional.of(error);
            }
        }

        return Optional.empty();
    }
}/*    e_bad_argument EXCEPTION;
    PRAGMA EXCEPTION_INIT (e_bad_argument, -100000);
    e_missing_argument EXCEPTION;
    PRAGMA EXCEPTION_INIT (e_missing_argument, -100001);
    e_no_such_row EXCEPTION;
    PRAGMA EXCEPTION_INIT (e_no_such_row, -100002);*/
