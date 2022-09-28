package chocolate.factory.gui.controls;

import chocolate.factory.model.WorkLog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkLogTable extends TableView<WorkLog> {

    public WorkLogTable(boolean showEmployeeInfo) {
        if (showEmployeeInfo) {
            createEmployeeInfoColumn();
        }
        createCommonColumns();
    }

    private void createCommonColumns() {
        TableColumn<WorkLog, LocalDate> dayColumn = new TableColumn<>("Date");
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("clockDate"));
        dayColumn.setCellFactory((v)-> new DateTableCell<>());
        TableColumn<WorkLog, LocalTime> clockInColumn = new TableColumn<>("In Time");
        clockInColumn.setCellValueFactory(new PropertyValueFactory<>("clockInTime"));
        clockInColumn.setCellFactory((v)-> new TimeTableCell<>());
        TableColumn<WorkLog, LocalTime> clockOutColumn = new TableColumn<>("Out Time");
        clockOutColumn.setCellValueFactory(new PropertyValueFactory<>("clockOutTime"));
        clockOutColumn.setCellFactory((v)-> new TimeTableCell<>());

        //noinspection unchecked
        getColumns().addAll(dayColumn, clockInColumn, clockOutColumn);
    }

    private void createEmployeeInfoColumn() {
        TableColumn<WorkLog, Integer> employeeId = new TableColumn<>("Employee ID");
        employeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));

        getColumns().add(employeeId);
    }
}
