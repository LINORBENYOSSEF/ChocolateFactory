package chocolate.factory.gui.view;

import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.gui.controls.AddWorkLogDialog;
import chocolate.factory.gui.controls.ChocolateButton;
import chocolate.factory.gui.controls.WorkLogTable;
import chocolate.factory.model.Employee;
import chocolate.factory.model.WorkLog;
import chocolate.factory.util.ImageLoader;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.util.List;
import java.util.Optional;

public class EmployeeView extends AbstractView {

    private final Property<Employee> currentEmployee = new SimpleObjectProperty<>();
    private final IntegerProperty completedHoursProp = new SimpleIntegerProperty();

    private final TableView<WorkLog> workLogTable;

    public EmployeeView(ImageLoader imageLoader) {
        workLogTable = new WorkLogTable(false);

        Label completedHoursLabel = new Label();
        Label expectedSalaryLabel = new Label();
        completedHoursProp.addListener((obs, o, n)-> {
            Employee employee = currentEmployee.getValue();
            completedHoursLabel.setText(String.format("Completed %d", n.intValue()));
            expectedSalaryLabel.setText(String.format("Expected salary: %f$",
                    n.intValue() * employee.getSalaryPerHourInDollars()));
        });

        HBox summeryPane = new HBox();
        summeryPane.setAlignment(Pos.CENTER_LEFT);
        summeryPane.setSpacing(5);
        summeryPane.setPadding(new Insets(5));
        summeryPane.getChildren().addAll(completedHoursLabel, expectedSalaryLabel);

        setTop(summeryPane);
        setCenter(workLogTable);

        Button addLog = new ChocolateButton("Add Work Log", imageLoader);
        addLog.setOnAction((e)-> {
            AddWorkLogDialog dialog = new AddWorkLogDialog();
            dialog.initOwner(getOwner());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Add Work Log");

            Optional<WorkLog> optional = dialog.showAndWait();
            if (optional.isPresent()) {
                WorkLog workLog = optional.get();
                workLog.setEmployeeId(currentEmployee.getValue().getId());

                try {
                    getDbOps().addWorkLog(workLog);
                } catch (DbOpsException ex) {
                    Dialogs.showExceptionDialog(getOwner(), "Error saving work log", ex);
                }

                loadData();
            }
        });

        VBox buttons = new VBox();
        buttons.getChildren().addAll(addLog);
        buttons.setSpacing(2);
        buttons.setPadding(new Insets(5));

        setRight(buttons);
    }

    @Override
    public Pane getRoot() {
        return this;
    }

    @Override
    public String getName() {
        return "Employee Portal";
    }

    @Override
    public boolean onEnter() {
        if (currentEmployee.getValue() == null) {
            Employee employee = showLogInDialog();
            if (employee == null) {
                return false;
            }
            currentEmployee.setValue(employee);
        }

        loadData();
        return true;
    }

    @Override
    public void onExit() {
        currentEmployee.setValue(null);
    }

    private Employee showLogInDialog() {
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
                Optional<Employee> logInOptional = getDbOps().getEmployeeByName(result.get());
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
            List<WorkLog> logs = getDbOps().getEmployeeWorkLogs(currentEmployee.getValue());
            workLogTable.getItems().clear();
            workLogTable.getItems().addAll(logs);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading work logs", e);
        }

        try {
            int completedHoursThisMonth = getDbOps().getWorkHoursCompletedThisMonth(currentEmployee.getValue());
            completedHoursProp.set(completedHoursThisMonth);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading completed hours", e);
        }
    }
}
