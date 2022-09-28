package chocolate.factory.gui.view;

import chocolate.factory.db.DbOpsException;
import chocolate.factory.gui.Dialogs;
import chocolate.factory.gui.controls.ChocolateButton;
import chocolate.factory.gui.controls.ChooseChocolateDialog;
import chocolate.factory.model.Chocolate;
import chocolate.factory.model.schemas.ChocolateOrderEntrySchema;
import chocolate.factory.util.ImageLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ClientNewOrderSubView extends AbstractView implements View {

    private final ObservableList<Chocolate> chocolateTypes;

    public ClientNewOrderSubView(Consumer<List<ChocolateOrderEntrySchema>> placeOrderCallable, ImageLoader imageLoader) {
        this.chocolateTypes = FXCollections.observableArrayList();

        TableColumn<ChocolateOrderEntrySchema, String> chocolateNameCol = new TableColumn<>("Chocolate");
        chocolateNameCol.setCellValueFactory(new PropertyValueFactory<>("chocolateName"));
        TableColumn<ChocolateOrderEntrySchema, Integer> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableView<ChocolateOrderEntrySchema> itemsTable = new TableView<>();
        //noinspection unchecked
        itemsTable.getColumns().addAll(chocolateNameCol, amountCol);
        itemsTable.setEditable(false);

        Button addEntry = new ChocolateButton("Add", imageLoader);
        addEntry.setOnAction((e)-> {
            ChooseChocolateDialog dialog = new ChooseChocolateDialog();
            dialog.initOwner(getOwner());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Add Chocolate");
            dialog.setChocolates(chocolateTypes);

            Optional<Pair<Chocolate, Integer>> optional = dialog.showAndWait();
            if (optional.isPresent()) {
                Pair<Chocolate, Integer> pair = optional.get();
                itemsTable.getItems().add(new ChocolateOrderEntrySchema(pair.getKey(), pair.getValue()));
            }
        });
        Button removeEntry = new ChocolateButton("Remove", imageLoader);
        removeEntry.setOnAction((e)-> {
            int index = itemsTable.getSelectionModel().getSelectedIndex();
            if (index > 0) {
                itemsTable.getItems().remove(index);
            }
        });

        itemsTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n)-> {
            removeEntry.setDisable(n == null);
        });

        Button placeOrder = new ChocolateButton("Place Order", imageLoader);
        placeOrder.setOnAction((e)-> {
            List<ChocolateOrderEntrySchema> entries = new ArrayList<>(itemsTable.getItems());
            placeOrderCallable.accept(entries);
        });

        setCenter(itemsTable);

        VBox controlsBtns = new VBox();
        controlsBtns.getChildren().addAll(addEntry, removeEntry);
        controlsBtns.setSpacing(2);
        controlsBtns.setPadding(new Insets(5));
        setRight(controlsBtns);

        VBox bottomBtns = new VBox();
        bottomBtns.getChildren().addAll(placeOrder);
        bottomBtns.setSpacing(2);
        bottomBtns.setPadding(new Insets(5));
        setBottom(bottomBtns);
    }

    @Override
    public boolean onEnter() {
        try {
            List<Chocolate> chocolates = getDbOps().getAllChocolates();
            chocolateTypes.clear();
            chocolateTypes.addAll(chocolates);
        } catch (DbOpsException e) {
            Dialogs.showExceptionDialog(getOwner(), "Error loading chocolate types", e);
        }

        return true;
    }

    @Override
    public void onExit() {

    }

    @Override
    public String getName() {
        return "New Order";
    }
}
