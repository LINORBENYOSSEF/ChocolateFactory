package chocolate.factory.gui.view;

import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.gui.controls.AddEmployeeDialog;
import chocolate.factory.gui.controls.ChocolateButton;
import chocolate.factory.model.Client;
import chocolate.factory.model.Employee;
import chocolate.factory.util.ImageLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.util.List;
import java.util.Optional;

public class ClientsDataSubView extends AbstractView {

    private final TableView<Client> table;

    public ClientsDataSubView(ImageLoader imageLoader) {
        TableColumn<Client, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Client, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table = new TableView<>();
        //noinspection unchecked
        table.getColumns().addAll(idColumn, nameColumn);

        Button addEmployee = new ChocolateButton("Add", imageLoader);
        addEmployee.setOnAction((e)-> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.initOwner(getOwner());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Add Client");
            dialog.setContentText("Client Name");

            Optional<String> optional = dialog.showAndWait();
            if (optional.isPresent()) {
                Client client = new Client();
                client.setName(optional.get());

                try {
                    getDbOps().addClient(client);
                } catch (DbOpsException ex) {
                    Dialogs.showExceptionDialog(getOwner(), "Error adding client", ex);
                }

                loadData();
            }
        });

        Button removeEmployee = new ChocolateButton("Remove", imageLoader);
        removeEmployee.setOnAction((e)-> {
            Client client = table.getSelectionModel().getSelectedItem();

            try {
                getDbOps().deleteClient(client);
            } catch (DbOpsException ex) {
                Dialogs.showExceptionDialog(getOwner(), "Error deleting client", ex);
            }

            loadData();
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            removeEmployee.setDisable(n == null);
        });

        VBox controlsBtns = new VBox();
        controlsBtns.getChildren().addAll(addEmployee, removeEmployee);
        controlsBtns.setSpacing(2);
        controlsBtns.setPadding(new Insets(5));
        setRight(controlsBtns);

        setCenter(table);
    }

    @Override
    public boolean onEnter() {
        loadData();

        return true;
    }

    @Override
    public void onExit() {

    }

    @Override
    public String getName() {
        return "Clients";
    }

    private void loadData() {
        try {
            List<Client> clients = getDbOps().getAllClients();
            table.getItems().clear();
            table.getItems().addAll(clients);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading clients", e);
        }
    }
}
