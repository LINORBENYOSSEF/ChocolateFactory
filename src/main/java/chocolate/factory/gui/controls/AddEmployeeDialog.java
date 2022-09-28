package chocolate.factory.gui.controls;

import chocolate.factory.model.Employee;
import chocolate.factory.model.EmployeePosition;
import chocolate.factory.model.Ingredient;
import chocolate.factory.model.schemas.NewEmployeeSchema;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AddEmployeeDialog extends Dialog<NewEmployeeSchema> {

    private final GridPane grid;
    private final TextField nameField;
    private final ComboBox<EmployeePosition> positionField;

    public AddEmployeeDialog() {
        final DialogPane dialogPane = getDialogPane();

        this.nameField = new TextField("");
        this.nameField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setFillWidth(nameField, true);

        this.positionField = new ComboBox<>();
        this.positionField.getItems().addAll(EmployeePosition.values());
        this.positionField.getSelectionModel().select(0);
        this.positionField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(positionField, Priority.ALWAYS);
        GridPane.setFillWidth(positionField, true);

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        nameField.textProperty().addListener((obs, o, n)-> {
            getDialogPane().lookupButton(ButtonType.OK).setDisable(n.isBlank());
        });
        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE) {
                NewEmployeeSchema employee = new NewEmployeeSchema();
                employee.setName(nameField.getText());
                employee.setPosition(positionField.getValue());

                return employee;
            }

            return null;
        });
    }

    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(createContentLabel("Name"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(createContentLabel("Position"), 0, 1);
        grid.add(positionField, 1, 1);
        getDialogPane().setContent(grid);

        Platform.runLater(nameField::requestFocus);
    }

    private static Label createContentLabel(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.getStyleClass().add("content");
        label.setWrapText(true);
        label.setPrefWidth(360);
        return label;
    }
}
