package chocolate.factory.gui.controls;

import chocolate.factory.model.Chocolate;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.util.List;

public class ChooseChocolateDialog extends Dialog<Pair<Chocolate, Integer>> {

    private final GridPane grid;
    private final ComboBox<Chocolate> chocolateField;
    private final NumericField amountField;

    public ChooseChocolateDialog() {
        final DialogPane dialogPane = getDialogPane();

        this.chocolateField = new ComboBox<>();
        this.chocolateField.setMaxWidth(Double.MAX_VALUE);
        this.chocolateField.setConverter(new StringConverter<Chocolate>() {
            @Override
            public String toString(Chocolate object) {
                return object != null ? object.getName() : "";
            }

            @Override
            public Chocolate fromString(String string) {
                for (Chocolate chocolate : chocolateField.getItems()) {
                    if (chocolate.getName().equals(string)) {
                        return chocolate;
                    }
                }

                return null;
            }
        });
        this.chocolateField.setCellFactory((v)-> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Chocolate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });
        GridPane.setHgrow(chocolateField, Priority.ALWAYS);
        GridPane.setFillWidth(chocolateField, true);

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

        chocolateField.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            getDialogPane().lookupButton(ButtonType.OK).setDisable(n == null);
        });
        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE) {
                return new Pair<>(chocolateField.getValue(), amountField.valueProperty().getValue().intValue());
            }

            return null;
        });
    }

    public void setChocolates(List<Chocolate> chocolates) {
        chocolateField.getItems().clear();
        chocolateField.getItems().addAll(chocolates);
    }

    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(chocolateField, 0, 0, 2, 1);
        grid.add(createContentLabel("Amount"), 0, 1);
        grid.add(amountField, 1, 1);
        getDialogPane().setContent(grid);
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
