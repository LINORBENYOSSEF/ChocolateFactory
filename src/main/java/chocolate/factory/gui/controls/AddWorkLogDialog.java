package chocolate.factory.gui.controls;

import chocolate.factory.model.WorkLog;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AddWorkLogDialog extends Dialog<WorkLog> {

    private final GridPane grid;
    private final DatePicker dateField;
    private final TimeSpinner inField;
    private final TimeSpinner outField;

    public AddWorkLogDialog() {
        final DialogPane dialogPane = getDialogPane();

        this.dateField = new DatePicker(LocalDate.now());
        this.dateField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(dateField, Priority.ALWAYS);
        GridPane.setFillWidth(dateField, true);

        this.inField = new TimeSpinner();
        this.inField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(inField, Priority.ALWAYS);
        GridPane.setFillWidth(inField, true);

        this.outField = new TimeSpinner();
        this.outField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(outField, Priority.ALWAYS);
        GridPane.setFillWidth(outField, true);

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE) {
                WorkLog workLog = new WorkLog();
                workLog.setClockInTime(LocalDateTime.of(dateField.getValue(), inField.getValue()));
                workLog.setClockOutTime(LocalDateTime.of(dateField.getValue(), outField.getValue()));

                return workLog;
            }

            return null;
        });
    }

    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(createContentLabel("Day"), 0, 0);
        grid.add(dateField, 1, 0);
        grid.add(createContentLabel("Clock In"), 0, 1);
        grid.add(inField, 1, 1);
        grid.add(createContentLabel("Clock Out"), 0, 2);
        grid.add(outField, 1, 2);
        getDialogPane().setContent(grid);

        Platform.runLater(dateField::requestFocus);
    }

    private static Label createContentLabel(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.getStyleClass().add("content");
        label.setWrapText(true);
        label.setPrefWidth(360);
        return label;
    }
}
