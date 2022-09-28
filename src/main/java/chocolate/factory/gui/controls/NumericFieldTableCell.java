package chocolate.factory.gui.controls;

import javafx.scene.control.TableCell;

public class NumericFieldTableCell<S,T> extends TableCell<S,T> {

    private final NumericField field;

    public NumericFieldTableCell(Class<? extends Number> type) {
        this.field = new NumericField(type);

        setEditable(true);
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            field.setText(getItem().toString());
            setText(null);
            setGraphic(field);
        }
    }
}