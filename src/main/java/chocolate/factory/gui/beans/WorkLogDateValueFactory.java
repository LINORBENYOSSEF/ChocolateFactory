package chocolate.factory.gui.beans;

import chocolate.factory.model.WorkLog;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class WorkLogDateValueFactory 
        implements Callback<TableColumn.CellDataFeatures<WorkLog, String>, ObservableValue<String>> {
    
    
    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<WorkLog, String> param) {
        return null;
    }
}
