package chocolate.factory.gui.view;

import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.gui.controls.AddEmployeeDialog;
import chocolate.factory.gui.controls.ChocolateButton;
import chocolate.factory.model.Employee;
import chocolate.factory.model.schemas.NewEmployeeSchema;
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

public class EmployeeDataSubView extends AbstractView {

    private final TableView<Employee> table;

    public EmployeeDataSubView(ImageLoader imageLoader) {
        TableColumn<Employee, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Employee, Double> salaryColumn = new TableColumn<>("Salary Per Hour [$]");
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salaryPerHourInDollars"));
        table = new TableView<>();
        //noinspection unchecked
        table.getColumns().addAll(idColumn, nameColumn, salaryColumn);

        Button addEmployee = new ChocolateButton("Add", imageLoader);
        addEmployee.setOnAction((e)-> {
            AddEmployeeDialog dialog = new AddEmployeeDialog();
            dialog.initOwner(getOwner());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Add Employee");

            Optional<NewEmployeeSchema> optional = dialog.showAndWait();
            if (optional.isPresent()) {
                NewEmployeeSchema employee = optional.get();

                try {
                    getDbOps().addEmployee(employee);
                } catch (DbOpsException ex) {
                    Dialogs.showExceptionDialog(getOwner(), "Error adding employee", ex);
                }

                loadData();
            }
        });

        Button removeEmployee = new ChocolateButton("Remove", imageLoader);
        removeEmployee.setOnAction((e)-> {
            Employee employee = table.getSelectionModel().getSelectedItem();

            try {
                getDbOps().deleteEmployee(employee);
            } catch (DbOpsException ex) {
                Dialogs.showExceptionDialog(getOwner(), "Error deleting employee", ex);
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
        return "Employees";
    }

    private void loadData() {
        try {
            List<Employee> employees = getDbOps().getEmployees();
            table.getItems().clear();
            table.getItems().addAll(employees);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading employees", e);
        }
    }
}
