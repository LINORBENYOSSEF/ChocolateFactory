package chocolate.factory.gui.controls;

import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.model.OrderStatus;
import chocolate.factory.model.views.ChocolateOrderEntryView;
import chocolate.factory.model.views.ChocolateOrderView;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.MasterDetailPane;

import java.time.LocalDate;
import java.util.List;

public class ChocolateOrderViewUneditableTable extends AbstractDataControl {

    private final ObservableList<ChocolateOrderView> orders;
    private final TableView<ChocolateOrderView> table;

    public ChocolateOrderViewUneditableTable(boolean showClientInfo, boolean showStatus) {
        TableColumn<ChocolateOrderView, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        TableColumn<ChocolateOrderView, LocalDate> dayColumn = new TableColumn<>("Date");
        dayColumn.setCellFactory((v)-> new DateTableCell<>());
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        table = new TableView<>();
        //noinspection unchecked
        table.getColumns().addAll(idColumn, dayColumn);

        if (showClientInfo) {
            TableColumn<ChocolateOrderView, String> clientIdColumn = new TableColumn<>("Client ID");
            clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
            TableColumn<ChocolateOrderView, String> clientName = new TableColumn<>("Client Name");
            clientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
            //noinspection unchecked
            table.getColumns().addAll(clientIdColumn, clientName);
        }

        if (showStatus) {
            TableColumn<ChocolateOrderView, OrderStatus> statusColumn = new TableColumn<>("Status");
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            table.getColumns().add(statusColumn);
        }

        this.orders = table.getItems();

        TableColumn<ChocolateOrderEntryView, String> chocolateNameCol = new TableColumn<>("Chocolate");
        chocolateNameCol.setCellValueFactory(new PropertyValueFactory<>("chocolateName"));
        TableColumn<ChocolateOrderEntryView, Integer> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableView<ChocolateOrderEntryView> itemsTable = new TableView<>();
        //noinspection unchecked
        itemsTable.getColumns().addAll(chocolateNameCol, amountCol);
        itemsTable.setEditable(false);

        MasterDetailPane masterDetailPane = new MasterDetailPane();
        masterDetailPane.setMasterNode(table);
        masterDetailPane.setDetailNode(itemsTable);
        masterDetailPane.setDetailSide(Side.BOTTOM);

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            if (n == null) {
                itemsTable.getItems().clear();
            } else {
                try {
                    // get order contents
                    List<ChocolateOrderEntryView> orderEntries = getDbOps().getChocolateOrderEntries(n.getOrderId());

                    itemsTable.getItems().clear();
                    itemsTable.getItems().addAll(orderEntries);
                } catch (DbOpsException e) {
                    Dialogs.showExceptionDialog(getOwner(), "Failed to get order items", e);
                }
            }
        });

        setCenter(masterDetailPane);
    }

    public void setOrders(List<? extends ChocolateOrderView> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
    }

    public SelectionModel<ChocolateOrderView> getSelectionModel() {
        return table.getSelectionModel();
    }
}
