package chocolate.factory.gui.view;

import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.gui.controls.ChocolateButton;
import chocolate.factory.gui.controls.DateTableCell;
import chocolate.factory.model.OrderStatus;
import chocolate.factory.model.views.IngredientOrderView;
import chocolate.factory.util.ImageLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class IngredientOrdersSubView extends AbstractView {

    private final TableView<IngredientOrderView> table;

    public IngredientOrdersSubView(ImageLoader imageLoader) {
        TableColumn<IngredientOrderView, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        TableColumn<IngredientOrderView, LocalDate> dayColumn = new TableColumn<>("Date");
        dayColumn.setCellFactory((v)-> new DateTableCell<>());
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<IngredientOrderView, String> ingredientIdColumn = new TableColumn<>("Ingredient ID");
        ingredientIdColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientId"));
        TableColumn<IngredientOrderView, String> ingredientNameColumn = new TableColumn<>("Ingredient Name");
        ingredientNameColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        TableColumn<IngredientOrderView, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<IngredientOrderView, OrderStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        table = new TableView<>();
        //noinspection unchecked
        table.getColumns().addAll(idColumn, dayColumn, ingredientIdColumn, ingredientNameColumn, amountColumn, statusColumn);

        Button confirmOrderComplete = new ChocolateButton("Confirm Complete", imageLoader);
        confirmOrderComplete.setOnAction((e)-> {
            IngredientOrderView orderView = table.getSelectionModel().getSelectedItem();

            try {
                getDbOps().setIngredientOrderComplete(orderView.getOrderId());
            } catch (DbOpsException ex) {
                Dialogs.showExceptionDialog(getOwner(), "Error modifying order status", ex);
            }

            loadData();
        });
        confirmOrderComplete.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            confirmOrderComplete.setDisable(n == null || n.getStatus() == OrderStatus.COMPLETED);
        });

        VBox buttons = new VBox();
        buttons.getChildren().addAll(confirmOrderComplete);
        buttons.setSpacing(2);
        buttons.setPadding(new Insets(5));

        setRight(buttons);

        setRight(buttons);
        setCenter(table);
    }

    @Override
    public String getName() {
        return "Ingredient Orders";
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
            List<IngredientOrderView> orders = getDbOps().getIngredientOrders();
            table.getItems().clear();
            table.getItems().addAll(orders);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading orders", e);
        }
    }
}
