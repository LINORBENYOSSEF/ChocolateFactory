package chocolate.factory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.BreadCrumbBar;

public class FxTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BreadCrumbBar<String> breadCrumbBar = new BreadCrumbBar<>();


        TreeItem<String> root = new TreeItem<String>("Root");

        TreeItem<String> item1 = new TreeItem<String>("Item 1");
        TreeItem<String> item11 = new TreeItem<String>("Item 1.1");
        TreeItem<String> item12 = new TreeItem<String>("Item 1.2");

        item1.getChildren().addAll(item11, item12);

        TreeItem<String> item2 = new TreeItem<String>("Item 2");

        root.getChildren().addAll(item1, item2);
        breadCrumbBar.setSelectedCrumb(item11);

        VBox box = new VBox();
        box.getChildren().add(breadCrumbBar);

        primaryStage.setScene(new Scene(box));
        primaryStage.show();
    }
}
