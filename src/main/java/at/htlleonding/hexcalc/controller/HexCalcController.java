package at.htlleonding.hexcalc.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class HexCalcController {
    @FXML
    private TextField display;

    @FXML
    private ListView<HistoryItem> historyView;

    private ObservableList<HistoryItem> history;

    private final StringProperty displayValue = new SimpleStringProperty("");

    private String lastEval = "";

    private boolean inputDisabled = false;

    @FXML
    private void initialize() {
        display.textProperty().bindBidirectional(displayValue);

        history = FXCollections.observableArrayList();
        historyView.setItems(history);

        // Add mouse click event handler
        historyView.setOnMouseClicked(this::handleHistoryDoubleClick);
    }

    @FXML
    private void handleButtonAction(javafx.event.ActionEvent event) {
        String value = ((javafx.scene.control.Button) event.getSource()).getText();

        if (inputDisabled)
        {
            if (value.equals("E")) {
                displayValue.set("");
                display.setDisable(false);

                inputDisabled = false;
            }
            return;
        }

        if ("0123456789ABCDEF.+-*/^()".contains(value)) {
            displayValue.set(displayValue.get() + value);
            return;
        }

        switch (value) {
            case "=":
                onEnterKeyPressed();
                break;
            case "x":
                displayValue.set("");
                break;
            case "X":
                displayValue.set("");
                history.clear();
                break;
        }
    }

    public void onEnterKeyPressed() {
        if (inputDisabled)
            return;

        String value = displayValue.get();

        String eval = null;
        try {
            eval = Calculator.calculate(value);
        } catch (DivisionByZeroException e) {
            display.setDisable(true);
            inputDisabled = true;

            showAlert();

            displayValue.set("EEEEEEEEEEEEEEEEEEEEEEEE");
            history.clear();
        }

        if (value.isEmpty() || eval == null)
            return;

        if (eval.equals(lastEval))
            return;

        lastEval = eval;

        history.add(new HistoryItem(value, eval));
        displayValue.set(eval);
    }

    private void handleHistoryDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Double-click
            HistoryItem selectedItem = historyView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                displayValue.set(selectedItem.expression);
            }
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("HexCalc");
        alert.setHeaderText(null);
        alert.setContentText("You have been permanently banned from using HexCalc");

        alert.showAndWait();
    }
}