package chocolate.factory;

import chocolate.factory.db.DbConnector;
import chocolate.factory.db.DbOperations;
import chocolate.factory.util.Resources;

import java.util.Properties;

public class test {

    public static void main(String[] args) throws Exception {
        Properties dbProperties = Resources.getProperties("db.properties");
        DbConnector connector = new DbConnector(dbProperties);
        DbOperations dbOperations = new DbOperations(connector);

        System.out.println(dbOperations.getAllClients());
    }
}
