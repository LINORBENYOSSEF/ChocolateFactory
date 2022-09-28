package chocolate.factory.gui.controls;

import javafx.collections.ObservableList;
import javafx.scene.control.Cell;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;


public class EditableComboBoxTableCell<S,T> extends TableCell<S,T> {

    private final ComboBox<T> comboBox;

    public EditableComboBoxTableCell(StringConverter<T> converter, ObservableList<T> items) {
        this.getStyleClass().add("combo-box-table-cell");
        this.comboBox = createComboBox(this, items, converter);
        this.comboBox.setEditable(true);

        setEditable(true);
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            comboBox.getSelectionModel().select(item);
            setText(null);
            setGraphic(comboBox);
        }
    }

    private static <T> ComboBox<T> createComboBox(final Cell<T> cell,
                                                  final ObservableList<T> items,
                                                  final StringConverter<T> converter) {
        ComboBox<T> comboBox = new ComboBox<T>(items);
        comboBox.setConverter(converter);
        comboBox.setMaxWidth(Double.MAX_VALUE);

        // setup listeners to properly commit any changes back into the data model.
        // First listener attempts to commit or cancel when the ENTER or ESC keys are released.
        // This is applicable in cases where the ComboBox is editable, and the user has
        // typed some input, and also when the ComboBox popup is showing.
        comboBox.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                tryComboBoxCommit(comboBox, cell);
            } else if (e.getCode() == KeyCode.ESCAPE) {
                cell.cancelEdit();
            }
        });

        // Second listener attempts to commit when the user is in the editor of
        // the ComboBox, and moves focus away.
        comboBox.getEditor().focusedProperty().addListener(o -> {
            if (!comboBox.isFocused()) {
                tryComboBoxCommit(comboBox, cell);
            }
        });

        return comboBox;
    }

    private static <T> void tryComboBoxCommit(ComboBox<T> comboBox, Cell<T> cell) {
        StringConverter<T> sc = comboBox.getConverter();
        if (comboBox.isEditable() && sc != null) {
            T value = sc.fromString(comboBox.getEditor().getText());
            cell.commitEdit(value);
        } else {
            cell.commitEdit(comboBox.getValue());
        }
    }
}
