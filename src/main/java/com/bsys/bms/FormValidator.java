package com.bsys.bms;

import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;

/**
 * A utility class for validating form inputs in a JavaFX application.
 */
public class FormValidator {

    /**
     * Validates a text field to ensure that it is not empty.
     *
     * @param textField The text field to validate.
     * @return true if the text field is not empty; false otherwise.
     */
    public static boolean validateTextField(TextField textField) throws IOException {
        if (textField.getText().isEmpty()) {
            textField.setStyle("-fx-border-color: red;");
            showAlert("Please fill in all required fields");
            return false;
        } else {
            textField.setStyle(null);
            return true;
        }
    }

    /**
     * Validates a text area to ensure that it is not empty.
     *
     * @param textArea The text field to validate.
     * @return true if the text field is not empty; false otherwise.
     */
    public static boolean validateTextArea(TextArea textArea) throws IOException {
        if (textArea.getText().isEmpty()) {
            textArea.setStyle("-fx-border-color: red;");
            showAlert("Please fill in all required fields");
            return false;
        } else {
            textArea.setStyle(null);
            return true;
        }
    }

    /**
     * Validates a combo box to ensure that an item is selected.
     *
     * @param comboBox The combo box to validate.
     * @return true if an item is selected; false otherwise.
     */
    public static boolean validateComboBox(ComboBox comboBox) throws IOException {
        if (comboBox.getSelectionModel().isEmpty()) {
            comboBox.setStyle("-fx-border-color: red;");
            showAlert("Please select an item from the dropdown");
            return false;
        } else {
            comboBox.setStyle(null);
            return true;
        }
    }

    /**
     * Validates a date picker to ensure that a date is selected and meets the required conditions.
     *
     * @param datePicker The date picker to validate.
     * @param otherDatePicker Another date picker representing the minimum date allowed.
     * @return true if a valid date is selected; false otherwise.
     */
    public static boolean validateDatePicker(DatePicker datePicker, DatePicker otherDatePicker) throws IOException {
        if (datePicker.getValue() == null) {
            datePicker.setStyle("-fx-border-color: red;");
            showAlert("Please select a date");
            return false;
        } else if (datePicker.getValue().isBefore(LocalDate.now())) {
            datePicker.setStyle("-fx-border-color: red;");
            showAlert("Date cannot be in the past");
            return false;
        } else if (otherDatePicker != null && otherDatePicker.getValue() != null && datePicker.getValue().isBefore(otherDatePicker.getValue())) {
            datePicker.setStyle("-fx-border-color: red;");
            showAlert("Date cannot be less than " + otherDatePicker.getValue().toString());
            return false;
        } else {
            datePicker.setStyle(null);
            return true;
        }
    }

    /**
     * Validates the input in the specified TextField as an integer value.
     * If the input is empty, an error message is displayed and the method returns false.
     * If the input is not a valid integer, an error message is displayed and the method returns false.
     * Otherwise, the method returns true.
     *
     * @param textField the TextField to validate
     * @return true if the input is a valid integer, false otherwise
     */
    public static boolean validateInteger(TextField textField) throws IOException {
        if (textField.getText().isEmpty()) {
            textField.setStyle("-fx-border-color: red;");
            showAlert("This field is required");
            return false;
        } else {
            try {
                textField.setStyle(null);
                Integer.parseInt(textField.getText());
                return true;
            } catch (NumberFormatException e) {
                textField.setStyle("-fx-border-color: red;");
                showAlert("Please enter a valid integer value");
                return false;
            }
        }
    }

    /**
     * Shows an alert message with the specified title and message.
     *
     * @param message The message of the alert.
     */
    private static void showAlert(String message) {
       AlertController.showAlert("error", message);
    }
}