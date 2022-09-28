package chocolate.factory.gui.view;

import chocolate.factory.db.DbOperations;
import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.gui.controls.ChocolateOrderViewUneditableTable;
import chocolate.factory.model.Client;
import chocolate.factory.model.views.ClosedChocolateOrderView;
import chocolate.factory.model.views.OpenChocolateOrderView;
import chocolate.factory.util.ImageLoader;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class ClientView extends AbstractView {

    private final Property<Client> currentClient = new SimpleObjectProperty<>();

    private final ChocolateOrderViewUneditableTable openOrdersTable;
    private final ChocolateOrderViewUneditableTable fulfilledOrdersTable;

    private final ClientNewOrderSubView newOrderView;

    public ClientView(ImageLoader imageLoader) {
        openOrdersTable = new ChocolateOrderViewUneditableTable(false, false);
        fulfilledOrdersTable = new ChocolateOrderViewUneditableTable(false, false);

        TabPane ordersPane = new TabPane();
        Tab openTab = new Tab("Open");
        openTab.setContent(openOrdersTable);
        openTab.setClosable(false);
        Tab fulfilledTab = new Tab("Fulfilled");
        fulfilledTab.setContent(fulfilledOrdersTable);
        fulfilledTab.setClosable(false);
        ordersPane.getTabs().addAll(openTab, fulfilledTab);

        ordersPane.getSelectionModel().select(0);

        this.newOrderView = new ClientNewOrderSubView((entries)-> {
            try {
                getDbOps().createChocolateOrder(currentClient.getValue(), entries);
                loadData();
            } catch (DbOpsException e) {
                Dialogs.showExceptionDialog(getOwner(), "Error placing order", e);
            }
        }, imageLoader);

        setCenter(ordersPane);
        setLeft(newOrderView);
    }

    @Override
    public void load(Stage stage, DbOperations dbOps) {
        super.load(stage, dbOps);
        openOrdersTable.load(stage, dbOps);
        fulfilledOrdersTable.load(stage, dbOps);
        newOrderView.load(stage, dbOps);
    }

    @Override
    public String getName() {
        return "Client Portal";
    }

    @Override
    public boolean onEnter() {
        if (currentClient.getValue() == null) {
            Client client = showLogInDialog();
            if (client == null) {
                return false;
            }
            currentClient.setValue(client);
        }

        loadData();

        if (!newOrderView.onEnter()) {
            return false;
        }

        openOrdersTable.getSelectionModel().select(-1);

        return true;
    }

    @Override
    public void onExit() {
        currentClient.setValue(null);
    }

    private Client showLogInDialog() {
        while (true) {
            TextInputDialog dialog = new TextInputDialog(null);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(getOwner());
            dialog.setTitle("Log In");
            dialog.setContentText("Please enter your name:");

            Optional<String> result = dialog.showAndWait();
            if (result.isEmpty()){
                return null;
            }

            try {
                Optional<Client> logInOptional = getDbOps().getClientByName(result.get());
                if (logInOptional.isEmpty()) {
                    Dialogs.showErrorDialog(getOwner(), "Bad LogIn", "No such log in");
                    continue;
                }

                return logInOptional.get();
            } catch (DbOpsException e) {
                Dialogs.showExceptionDialog(getOwner(), "Exception with querying login object", e);
            }
        }
    }

    private void loadData() {
        try {
            List<OpenChocolateOrderView> orders = getDbOps()
                    .getOpenChocolateOrdersForClient(currentClient.getValue());
            openOrdersTable.setOrders(orders);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading open orders", e);
        }

        try {
            List<ClosedChocolateOrderView> orders = getDbOps()
                    .getFulfilledChocolateOrdersForClient(currentClient.getValue());
            fulfilledOrdersTable.setOrders(orders);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading fulfilled orders", e);
        }
    }
}
