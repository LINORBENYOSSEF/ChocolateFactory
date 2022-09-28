package chocolate.factory.gui.controls;

import chocolate.factory.model.Ingredient;
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

public class ChooseIngredientDialog extends Dialog<Pair<Ingredient, Integer>> {

    private final GridPane grid;
    private final ComboBox<Ingredient> ingredientField;
    private final NumericField amountField;

    public ChooseIngredientDialog() {
        final DialogPane dialogPane = getDialogPane();

        this.ingredientField = new ComboBox<>();
        this.ingredientField.setMaxWidth(Double.MAX_VALUE);
        this.ingredientField.setConverter(new StringConverter<Ingredient>() {
            @Override
            public String toString(Ingredient object) {
                return object != null ? object.getName() : "";
            }

            @Override
            public Ingredient fromString(String string) {
                for (Ingredient ingredient : ingredientField.getItems()) {
                    if (ingredient.getName().equals(string)) {
                        return ingredient;
                    }
                }

                return null;
            }
        });
        this.ingredientField.setCellFactory((v)-> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Ingredient item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });
        GridPane.setHgrow(ingredientField, Priority.ALWAYS);
        GridPane.setFillWidth(ingredientField, true);

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

        ingredientField.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            getDialogPane().lookupButton(ButtonType.OK).setDisable(n == null);
        });
        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE) {
                return new Pair<>(ingredientField.getValue(), amountField.valueProperty().getValue().intValue());
            }

            return null;
        });
    }

    public void setIngredients(List<Ingredient> ingredients) {
        ingredientField.getItems().clear();
        ingredientField.getItems().addAll(ingredients);
    }

    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(ingredientField, 0, 0, 2, 1);
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
