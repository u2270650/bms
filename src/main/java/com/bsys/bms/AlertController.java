package com.bsys.bms;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**

 AlertController class provides a simple way to display an alert dialog box

 with specified alert type, title, message and an optional icon.
 */
public class AlertController {

    /**

     Constructor that creates an alert dialog box with specified alert type, title and message.

     @param alertType the type of the alert, can be one of Alert.AlertType enum values.

     @param title the title of the alert dialog box.

     @param message the message to display in the alert dialog box.

     @throws IOException if an error occurs while reading the icon image file.
     */
    public AlertController(Object alertType, String title, String message) throws IOException {
        // check if both title and message are not null
        if (title != null && message != null) {
            // create a new Alert object with specified alert type
            Alert alert = new Alert((Alert.AlertType) alertType);

            // set the title and message of the alert dialog box
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            // load the icon image from resource file and set it to the alert dialog box
            URL iconPath = Main.class.getResource("images/logo.png");
            if (iconPath != null) {
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(iconPath.openStream()));
            }

            // display the alert dialog box and wait for user response
            alert.showAndWait();
        }
    }
}
