package chocolate.factory.gui.view;

import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.gui.controls.AddIngredientDialog;
import chocolate.factory.gui.controls.ChocolateButton;
import chocolate.factory.gui.controls.NumberInputDialog;
import chocolate.factory.model.Chocolate;
import chocolate.factory.model.Ingredient;
import chocolate.factory.util.ImageLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.util.List;
import java.util.Optional;

public class IngredientsSubView extends AbstractView {

    private final TableView<Ingredient> table;

    public IngredientsSubView(ImageLoader imageLoader) {
        TableColumn<Ingredient, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Ingredient, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Ingredient, String> priceColumn = new TableColumn<>("Price [$]");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceInDollars"));
        TableColumn<Ingredient, String> amountColumn = new TableColumn<>("Amount in Inventory");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        table = new TableView<>();
        //noinspection unchecked
        table.getColumns().addAll(idColumn, nameColumn, priceColumn, amountColumn);

        Button addIngredient = new ChocolateButton("Add Ingredient", imageLoader);
        addIngredient.setOnAction((e)-> {
            AddIngredientDialog dialog = new AddIngredientDialog();
            dialog.initOwner(getOwner());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Add Ingredient");

            Optional<Ingredient> optional = dialog.showAndWait();
            if (optional.isPresent()) {
                try {
                    getDbOps().addIngredient(optional.get());
                } catch (DbOpsException ex) {
                    Dialogs.showExceptionDialog(getOwner(), "Error adding ingredient", ex);
                }
            }

            loadData();
        });

        Button deleteIngredient = new ChocolateButton("Delete Ingredient", imageLoader);
        deleteIngredient.setOnAction((e)-> {
            Ingredient ingredient = table.getSelectionModel().getSelectedItem();
            try {
                getDbOps().deleteIngredient(ingredient);
            } catch (DbOpsException ex) {
                Dialogs.showExceptionDialog(getOwner(), "Error deleting ingredient", ex);
            }

            loadData();
        });

        Button placeOrder = new ChocolateButton("Place Order", imageLoader);
        placeOrder.setOnAction((e)-> {
            Ingredient ingredient = table.getSelectionModel().getSelectedItem();

            NumberInputDialog dialog = new NumberInputDialog();
            dialog.initOwner(getOwner());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Place Order");
            dialog.setContentText("Enter amount to order:");

            Optional<Integer> optionalInteger = dialog.showAndWait();
            if (optionalInteger.isPresent()) {
                try {
                    getDbOps().createIngredientOrder(ingredient, optionalInteger.get());
                } catch (DbOpsException ex) {
                    Dialogs.showExceptionDialog(getOwner(), "Error placing order", ex);
                }
            }
        });
        placeOrder.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            placeOrder.setDisable(n == null);
            deleteIngredient.setDisable(n == null);
        });

        VBox buttons = new VBox();
        buttons.getChildren().addAll(addIngredient, deleteIngredient, placeOrder);
        buttons.setSpacing(2);
        buttons.setPadding(new Insets(5));

        setRight(buttons);
        setCenter(table);
    }

    @Override
    public String getName() {
        return "Ingredients";
    }

    @Override
    public boolean onEnter() {
        loadData();

        return true;
    }

    @Override
    public void onExit() {

    }

    private void loadData() {
        try {
            List<Ingredient> ingredients = getDbOps().getAllIngredients();
            table.getItems().clear();
            table.getItems().addAll(ingredients);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading ingredients", e);
        }
    }
}
