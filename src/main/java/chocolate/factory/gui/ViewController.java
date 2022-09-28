package chocolate.factory.gui;

import chocolate.factory.db.DbOperations;
import chocolate.factory.gui.view.View;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.BreadCrumbBar;

public class ViewController extends VBox {

    private final Stage owner;
    private final DbOperations dbOperations;
    private final Pane viewPane;
    private final BreadCrumbBar<View> bar;

    private final Property<TreeItem<View>> rootView = new SimpleObjectProperty<>();
    private final Property<View> currentView = new SimpleObjectProperty<>();

    public ViewController(Stage owner, DbOperations dbOperations, Pane viewPane) {
        this.owner = owner;
        this.dbOperations = dbOperations;
        this.viewPane = viewPane;
        bar = new BreadCrumbBar<>();
        //bar.setSelectedCrumb();

        bar.setCrumbFactory((i)->
                new BreadCrumbBar.BreadCrumbButton(i.getValue() != null ? i.getValue().getName() : ""));
        bar.setOnCrumbAction((e)-> {
            setView(e.getSelectedCrumb().getValue());
        });

        getChildren().add(bar);
    }

    public void setRoot(View view) {
        TreeItem<View> item = compileTree(view);
        rootView.setValue(item);
    }

    public void setView(View view) {
        view.load(owner, dbOperations);
        if (!view.onEnter()) {
            return;
        }

        View current = currentView.getValue();
        if (current != null && !isSubView(current, view)) {
            unwindView(current, view);
        }

        currentView.setValue(view);

        TreeItem<View> item = findTree(view);
        bar.setSelectedCrumb(item);

        this.viewPane.getChildren().clear();
        this.viewPane.getChildren().add(view.getRoot());
    }

    private TreeItem<View> compileTree(View view) {
        TreeItem<View> root = new TreeItem<>(view);
        for (View child : view.getChildViews()) {
            root.getChildren().add(compileTree(child));
        }

        return root;
    }

    private TreeItem<View> findTree(View view) {
        return findTree(view, rootView.getValue());
    }

    private TreeItem<View> findTree(View view, TreeItem<View> root) {
        if (root.getValue().equals(view)) {
            return root;
        }
        for (TreeItem<View> child : root.getChildren()) {
            TreeItem<View> item = findTree(view, child);
            if (item != null) {
                return item;
            }
        }

        return null;
    }

    private boolean isSubView(View parent, View other) {
        for (View child : parent.getChildViews()) {
            if (child.equals(other)) {
                return true;
            }
        }

        return false;
    }

    private boolean unwindView(View currentView, View newView) {
        for (View child : newView.getChildViews()) {
            if (child.equals(currentView)) {
                child.onExit();
                return true;
            }

            if (unwindView(currentView, child)) {
                child.onExit();
                return true;
            }
        }

        return false;
    }
}
