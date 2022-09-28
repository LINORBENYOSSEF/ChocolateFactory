package chocolate.factory.gui.view;

import chocolate.factory.db.DbOperations;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public abstract class AbstractView extends BorderPane implements View {

    private final Property<Stage> owner = new SimpleObjectProperty<>();
    private final Property<DbOperations> dbOps = new SimpleObjectProperty<>();
    private final ObservableList<View> childViews = FXCollections.observableArrayList();


    public Stage getOwner() {
        return owner.getValue();
    }

    public DbOperations getDbOps() {
        return dbOps.getValue();
    }

    @Override
    public Node getRoot() {
        return this;
    }

    @Override
    public ObservableList<View> getChildViews() {
        return childViews;
    }

    @Override
    public void load(Stage stage, DbOperations dbOperations) {
        this.owner.setValue(stage);
        this.dbOps.setValue(dbOperations);
    }
}
