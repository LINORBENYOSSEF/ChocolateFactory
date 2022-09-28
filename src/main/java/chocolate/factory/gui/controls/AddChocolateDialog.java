package chocolate.factory.gui.controls;

import chocolate.factory.model.Chocolate;
import chocolate.factory.model.Ingredient;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AddChocolateDialog extends Dialog<Chocolate> {

    private final GridPane grid;
    private final TextField nameField;
    private final NumericField priceField;
    private final NumericField amountField;

    public AddChocolateDialog() {
        final DialogPane dialogPane = getDialogPane();

        this.nameField = new TextField("");
        this.nameField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setFillWidth(nameField, true);

        this.priceField = new NumericField(Double.class);
        this.priceField.setMaxWidth(Double.MAX_VALUE);
        this.priceField.valueProperty().setValue(1);
        this.priceField.setText("1");
        GridPane.setHgrow(priceField, Priority.ALWAYS);
        GridPane.setFillWidth(priceField, true);

        this.amountField = new NumericField(Integer.class);
        this.amountField.setMaxWidth(Double.MAX_VALUE);
        this.amountField.valueProperty().setValue(0);
        this.amountField.setText("0");
        GridPane.setHgrow(amountField, Priority.ALWAYS);
        GridPane.setFillWidth(amountField, true);

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
                Chocolate chocolate = new Chocolate();
                chocolate.setName(nameField.getText());
                chocolate.setSellPriceInDollars(priceField.valueProperty().getValue().doubleValue());
                chocolate.setAmountPresentInInventory(amountField.valueProperty().getValue().intValue());

                return chocolate;
            }

            return null;
        });
    }

    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(createContentLabel("Name"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(createContentLabel("Price [$]"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(createContentLabel("Amount"), 0, 2);
        grid.add(amountField, 1, 2);
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
