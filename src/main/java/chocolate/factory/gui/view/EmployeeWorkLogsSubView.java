package chocolate.factory.gui.view;

import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.gui.controls.WorkLogTable;
import chocolate.factory.model.Employee;
import chocolate.factory.model.WorkLog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class EmployeeWorkLogsSubView extends AbstractView {

    private final WorkLogTable allWorkLogs;
    private final WorkLogTable specificEmployeeWorkLogs;
    private final ComboBox<Employee> employees;

    public EmployeeWorkLogsSubView() {
        this.allWorkLogs = new WorkLogTable(true);
        this.specificEmployeeWorkLogs = new WorkLogTable(false);
        this.employees = new ComboBox<>();
        this.employees.setCellFactory((v)-> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Employee item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });

        this.employees.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            try {
                List<WorkLog> workLogs = getDbOps().getEmployeeWorkLogs(n);
                specificEmployeeWorkLogs.getItems().clear();
                specificEmployeeWorkLogs.getItems().addAll(workLogs);
            } catch (DbOpsException e) {
                Dialogs.showExceptionDialog(getOwner(), "Error loading work logs", e);
            }
        });

        VBox specificPane = new VBox();
        specificPane.setSpacing(2);
        specificPane.getChildren().addAll(employees, specificEmployeeWorkLogs);

        SplitPane pane = new SplitPane();
        pane.getItems().addAll(allWorkLogs, specificPane);
        setCenter(pane);
    }

    @Override
    public boolean onEnter() {
        try {
            List<Employee> employees = getDbOps().getEmployees();
            this.employees.getItems().clear();
            this.employees.getItems().addAll(employees);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading employees", e);
        }

        try {
            List<WorkLog> workLogs = getDbOps().getEmployeeWorkLogs();
            allWorkLogs.getItems().clear();
            allWorkLogs.getItems().addAll(workLogs);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading work logs", e);
        }

        return true;
    }

    @Override
    public void onExit() {

    }

    @Override
    public String getName() {
        return "Work Logs";
    }
}
