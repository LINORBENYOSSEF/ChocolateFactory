package chocolate.factory.gui.controls;

import chocolate.factory.db.DbOperations;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AbstractDataControl extends BorderPane {

    private final Property<Stage> owner = new SimpleObjectProperty<>();
    private final Property<DbOperations> dbOps = new SimpleObjectProperty<>();

    public Stage getOwner() {
        return owner.getValue();
    }

    public DbOperations getDbOps() {
        return dbOps.getValue();
    }

    public void load(Stage stage, DbOperations dbOperations) {
        this.owner.setValue(stage);
        this.dbOps.setValue(dbOperations);
    }
}
