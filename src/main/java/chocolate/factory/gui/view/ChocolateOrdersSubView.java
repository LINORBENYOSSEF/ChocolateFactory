package chocolate.factory.gui.view;

import chocolate.factory.db.DbOperations;
import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.gui.controls.ChocolateButton;
import chocolate.factory.gui.controls.ChocolateOrderViewUneditableTable;
import chocolate.factory.model.OrderStatus;
import chocolate.factory.model.views.ChocolateOrderView;
import chocolate.factory.model.views.IngredientOrderView;
import chocolate.factory.util.ImageLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ChocolateOrdersSubView extends AbstractView {

    private final ChocolateOrderViewUneditableTable orders;

    public ChocolateOrdersSubView(ImageLoader imageLoader) {
        orders = new ChocolateOrderViewUneditableTable(true, true);

        Button confirmOrderComplete = new ChocolateButton("Confirm Complete", imageLoader);
        confirmOrderComplete.setOnAction((e)-> {
            ChocolateOrderView orderView = orders.getSelectionModel().getSelectedItem();

            try {
                getDbOps().setChocolateOrderComplete(orderView.getOrderId());
            } catch (DbOpsException ex) {
                Dialogs.showExceptionDialog(getOwner(), "Error modifying order status", ex);
            }

            loadData();
        });
        confirmOrderComplete.setDisable(true);

        orders.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            confirmOrderComplete.setDisable(n == null || n.getStatus() == OrderStatus.COMPLETED);
        });

        VBox buttons = new VBox();
        buttons.getChildren().addAll(confirmOrderComplete);
        buttons.setSpacing(2);
        buttons.setPadding(new Insets(5));

        setRight(buttons);
        setCenter(orders);
    }

    @Override
    public void load(Stage stage, DbOperations dbOperations) {
        super.load(stage, dbOperations);
        orders.load(stage, dbOperations);
    }

    @Override
    public String getName() {
        return "Chocolate Orders";
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
            List<? extends ChocolateOrderView> orders = getDbOps().getChocolateOrders();
            this.orders.setOrders(orders);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading orders", e);
        }
    }
}
