package com.bsys.bms;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AlertController {

    private static final String ICON_PATH = "images/logo.png";

    /**
     * Constructor that creates an alert dialog box with specified alert type, title and message.
     *
     * @param title     the title of the alert dialog box.
     * @param message   the message to display in the alert dialog box.
     */
    public AlertController(String title, String message) {
        showAlert(title, message);
    }

    /**
     * Static method that displays an alert dialog box with specified alert type, title, and message.
     *
     * @param title     the title of the alert dialog box.
     * @param message   the message to display in the alert dialog box.
     */
    public static void showAlert(String title, String message) {
        if (title != null && message != null) {
            Alert.AlertType alertType = Alert.AlertType.NONE;

            if (title.equalsIgnoreCase("Information")) {
                alertType = Alert.AlertType.INFORMATION;
            } else if (title.equalsIgnoreCase("Warning")) {
                alertType = Alert.AlertType.WARNING;
            } else if (title.equalsIgnoreCase("Success")) {
                alertType = Alert.AlertType.CONFIRMATION;
            } else if (title.equalsIgnoreCase("Error")) {
                alertType = Alert.AlertType.ERROR;
            }

            Alert alert = new Alert(alertType);
            alert.setTitle(title.toUpperCase());
            alert.setHeaderText(null);
            alert.setContentText(message);

            URL path = AlertController.class.getResource(ICON_PATH);
            if (path != null) {
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(path.toExternalForm()));
            }

            alert.showAndWait();
        }
    }

}