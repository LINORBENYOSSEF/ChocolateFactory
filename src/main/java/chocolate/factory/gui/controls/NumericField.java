package chocolate.factory.gui.controls;

import javafx.beans.binding.NumberExpression;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;

import java.math.BigInteger;

public class NumericField extends TextField {

    private final NumericValidator<? extends Number> mValue;

    public NumericField(Class<? extends Number> type) {
        if ( type == byte.class || type == Byte.class || type == short.class || type == Short.class ||
                type ==	int.class  || type == Integer.class || type == long.class || type == Long.class ||
                type == BigInteger.class) {
            mValue = new LongValidator(this);
        } else {
            mValue = new DoubleValidator(this);
        }

        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                mValue.setValue(mValue.toNumber(getText()));
            }
        });
    }

    public final Property<Number> valueProperty() {
        return mValue;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (replaceValid(start, end, text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        IndexRange range = getSelection();
        if (replaceValid(range.getStart(), range.getEnd(), text)) {
            super.replaceSelection(text);
        }
    }

    private Boolean replaceValid(int start, int end, String fragment) {
        try {
            String newText = getText().substring(0, start) + fragment + getText().substring(end);
            if (newText.isEmpty()) return true;
            mValue.toNumber(newText);
            return true;
        } catch( Throwable ex ) {
            return false;
        }
    }


    private interface NumericValidator<T extends Number> extends NumberExpression, Property<Number> {
        void setValue(Number num);
        T toNumber(String s);

    }

    static class DoubleValidator extends SimpleDoubleProperty implements NumericValidator<Double> {

        private NumericField mField;

        public DoubleValidator(NumericField field) {
            super(field, "value", 0.0); //$NON-NLS-1$
            this.mField = field;
        }

        @Override
        protected void invalidated() {
            mField.setText(Double.toString(get()));
        }

        @Override
        public Double toNumber(String s) {
            if ( s == null || s.trim().isEmpty() ) return 0d;
            String d = s.trim();
            if ( d.endsWith("f") || d.endsWith("d") || d.endsWith("F") || d.endsWith("D") ) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                throw new NumberFormatException("There should be no alpha symbols"); //$NON-NLS-1$
            }
            return new Double(d);
        };

    }


    static class LongValidator extends SimpleLongProperty implements NumericValidator<Long> {

        private NumericField mField;

        public LongValidator(NumericField field) {
            super(field, "value", 0L); //$NON-NLS-1$
            this.mField = field;
        }

        @Override
        protected void invalidated() {
            mField.setText(Long.toString(get()));
        }

        @Override
        public Long toNumber(String s) {
            if ( s == null || s.trim().isEmpty() ) return 0L;
            String d = s.trim();
            return new Long(d);
        }

    }
}
