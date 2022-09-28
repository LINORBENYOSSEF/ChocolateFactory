package chocolate.factory;

import chocolate.factory.db.DbConnector;
import chocolate.factory.db.DbOperations;
import chocolate.factory.gui.MainWindow;
import chocolate.factory.util.ImageLoader;
import chocolate.factory.util.Resources;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ImageLoader imageLoader = new ImageLoader();
        Properties dbProperties = Resources.getProperties("db.properties");
        DbConnector connector = new DbConnector(dbProperties);
        MainWindow mainWindow = new MainWindow(primaryStage, new DbOperations(connector), imageLoader);
        primaryStage.setScene(new Scene(mainWindow, 700, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
