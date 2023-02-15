package com.bsys.bms;

import javafx.scene.control.Alert;

public class AlertConrtoller {
    public AlertConrtoller(Object alertType, String title, String message) {
        if(title != null && message != null) {
            Alert alert = new Alert((Alert.AlertType) alertType);
            alert.setTitle(title);
            alert.setHeaderText(message);
            alert.showAndWait();
        }
    }
}
