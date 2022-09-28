package chocolate.factory.gui.view;

import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.gui.controls.AddChocolateDialog;
import chocolate.factory.gui.controls.ChocolateButton;
import chocolate.factory.gui.controls.ChooseIngredientDialog;
import chocolate.factory.model.Chocolate;
import chocolate.factory.model.Ingredient;
import chocolate.factory.model.views.IngredientInChocolateView;
import chocolate.factory.util.ImageLoader;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Pair;
import org.controlsfx.control.MasterDetailPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChocolatesSubView extends AbstractView {

    private final TableView<Chocolate> table;
    private final TableView<IngredientInChocolateView> ingredientTable;
    private final List<Ingredient> ingredients;

    public ChocolatesSubView(ImageLoader imageLoader) {
        this.ingredients = new ArrayList<>();

        TableColumn<Chocolate, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Chocolate, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Chocolate, String> priceColumn = new TableColumn<>("Price [$]");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("sellPriceInDollars"));
        TableColumn<Chocolate, String> amountColumn = new TableColumn<>("Amount in Inventory");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountPresentInInventory"));
        table = new TableView<>();
        //noinspection unchecked
        table.getColumns().addAll(idColumn, nameColumn, priceColumn, amountColumn);

        TableColumn<IngredientInChocolateView, String> ingredientIdColumn = new TableColumn<>("ID");
        ingredientIdColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientId"));
        TableColumn<IngredientInChocolateView, String> ingredientNameColumn = new TableColumn<>("Name");
        ingredientNameColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        TableColumn<IngredientInChocolateView, String> ingredientAmountColumn = new TableColumn<>("Amount in Inventory");
        ingredientAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        ingredientTable = new TableView<>();
        //noinspection unchecked
        ingredientTable.getColumns().addAll(ingredientIdColumn, ingredientNameColumn, ingredientAmountColumn);

        Button addChocolate = new ChocolateButton("Add Chocolate", imageLoader);
        addChocolate.setOnAction((e)-> {
            AddChocolateDialog dialog = new AddChocolateDialog();
            dialog.initOwner(getOwner());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Add Chocolate");

            Optional<Chocolate> optional = dialog.showAndWait();
            if (optional.isPresent()) {
                try {
                    getDbOps().addChocolate(optional.get());
                } catch (DbOpsException ex) {
                    Dialogs.showExceptionDialog(getOwner(), "Error adding chocolate", ex);
                }
            }

            loadData();
        });

        Button deleteChocolate = new ChocolateButton("Delete Chocolate", imageLoader);
        deleteChocolate.setOnAction((e)-> {
            Chocolate chocolate = table.getSelectionModel().getSelectedItem();
            try {
                getDbOps().deleteChocolate(chocolate);
            } catch (DbOpsException ex) {
                Dialogs.showExceptionDialog(getOwner(), "Error deleting chocolate", ex);
            }

            loadData();
        });

        Button addIngredient = new ChocolateButton("Add Ingredient", imageLoader);
        addIngredient.setOnAction((e)-> {
            Chocolate chocolate = table.getSelectionModel().getSelectedItem();

            ChooseIngredientDialog dialog = new ChooseIngredientDialog();
            dialog.initOwner(getOwner());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Add Ingredient");
            dialog.setIngredients(ingredients);

            Optional<Pair<Ingredient, Integer>> optional = dialog.showAndWait();
            if (optional.isPresent()) {
                try {
                    Pair<Ingredient, Integer> result = optional.get();
                    getDbOps().addIngredientToChocolate(chocolate.getId(), result.getKey(), result.getValue());
                } catch (DbOpsException ex) {
                    Dialogs.showExceptionDialog(getOwner(), "Error adding ingredient to chocolate", ex);
                }
            }

            loadIngredientData(chocolate);
        });

        Button deleteIngredient = new ChocolateButton("Delete Ingredient", imageLoader);
        deleteIngredient.setOnAction((e)-> {
            IngredientInChocolateView ingredient = ingredientTable.getSelectionModel().getSelectedItem();

            try {
                getDbOps().removeIngredientFromChocolate(ingredient.getChocolateId(), ingredient.getIngredientId());
            } catch (DbOpsException ex) {
                Dialogs.showExceptionDialog(getOwner(), "Error removing ingredient from chocolate", ex);
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            if (n == null) {
                deleteChocolate.setDisable(true);
                addIngredient.setDisable(true);
                ingredientTable.getItems().clear();
            } else {
                deleteChocolate.setDisable(false);
                addIngredient.setDisable(false);
                loadIngredientData(n);
            }
        });
        deleteChocolate.setDisable(true);
        addIngredient.setDisable(true);
        deleteIngredient.setDisable(true);

        ingredientTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            deleteIngredient.setDisable(n == null);
        });

        MasterDetailPane masterDetailPane = new MasterDetailPane();
        masterDetailPane.setMasterNode(table);
        masterDetailPane.setDetailNode(ingredientTable);
        masterDetailPane.setDetailSide(Side.BOTTOM);

        VBox buttons = new VBox();
        buttons.getChildren().addAll(addChocolate, deleteChocolate, addIngredient, deleteIngredient);
        buttons.setSpacing(2);
        buttons.setPadding(new Insets(2));

        setRight(buttons);
        setCenter(masterDetailPane);
    }

    @Override
    public String getName() {
        return "Chocolates";
    }

    @Override
    public boolean onEnter() {
        try {
            List<Ingredient> ingredients = getDbOps().getAllIngredients();
            this.ingredients.clear();
            this.ingredients.addAll(ingredients);
        } catch (DbOpsException ex) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading ingredients", ex);
        }

        loadData();

        return true;
    }

    @Override
    public void onExit() {

    }

    private void loadData() {
        try {
            List<Chocolate> chocolates = getDbOps().getAllChocolates();
            table.getItems().clear();
            table.getItems().addAll(chocolates);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading chocolate", e);
        }
    }

    private void loadIngredientData(Chocolate chocolate) {
        try {
            List<IngredientInChocolateView> ingredients = getDbOps().getIngredientsIn(chocolate);

            ingredientTable.getItems().clear();
            ingredientTable.getItems().addAll(ingredients);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Failed to ingredients", e);
        }
    }
}
