package chocolate.factory.gui;

import chocolate.factory.db.DbOperations;
import chocolate.factory.gui.view.RootView;
import chocolate.factory.util.ImageLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainWindow extends BorderPane {

    private final StackPane currentView;

    public MainWindow(Stage owner, DbOperations dbOps, ImageLoader imageLoader) {
        this.currentView = new StackPane();

        ViewController controller = new ViewController(owner, dbOps, currentView);

        setCenter(currentView);

        HBox top = new HBox();
        top.getChildren().add(controller);
        top.setSpacing(2);
        top.setPadding(new Insets(5));
        top.setAlignment(Pos.CENTER_LEFT);
        setTop(top);

        RootView rootView = new RootView(controller, imageLoader);
        controller.setRoot(rootView);
        controller.setView(rootView);

        String image = MainWindow.class.getResource("/images/image.png").toExternalForm();
        setStyle("-fx-background-image: url('" + image + "'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;" +
                "-fx-background-size: cover;");
    }
}
