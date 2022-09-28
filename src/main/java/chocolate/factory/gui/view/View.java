package chocolate.factory.gui.view;

import chocolate.factory.db.DbOperations;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Stage;

public interface View {

    Node getRoot();
    String getName();
    ObservableList<View> getChildViews();

    void load(Stage stage, DbOperations dbOperations);

    boolean onEnter();
    void onExit();
}
