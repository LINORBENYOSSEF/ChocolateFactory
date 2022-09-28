package chocolate.factory.gui.view;

import chocolate.factory.gui.ViewController;
import chocolate.factory.gui.controls.ChocolateButton;
import chocolate.factory.util.ImageLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class RootView extends AbstractView {

    public RootView(ViewController viewController, ImageLoader imageLoader) {
        ClientView clientView = new ClientView(imageLoader);
        EmployeeView employeeView = new EmployeeView(imageLoader);
        ManagerView managerView = new ManagerView(viewController, imageLoader);

        getChildViews().addAll(clientView, employeeView, managerView);

        Button client = new ChocolateButton("Client Portal", imageLoader);
        client.setOnAction((e)-> {
            viewController.setView(clientView);
        });
        Button employee = new ChocolateButton("Employee Portal", imageLoader);
        employee.setOnAction((e)-> {
            viewController.setView(employeeView);
        });
        Button manager = new ChocolateButton("Manager Portal", imageLoader);
        manager.setOnAction((e)-> {
            viewController.setView(managerView);
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(client, employee, manager);
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(5));

        setCenter(hBox);
    }

    @Override
    public String getName() {
        return "Root";
    }

    @Override
    public boolean onEnter() {
        return true;
    }

    @Override
    public void onExit() {

    }
}
