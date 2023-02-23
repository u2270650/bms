package com.bsys.bms;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * SceneController class is responsible for managing the scenes of the application.
 * The class has two main functions:
 * createWindow: This function creates a new window with the specified title, fxml file name, width, and height.
 * changeScene: This function changes the current scene of the application to the specified fxml file.
 */

public class SceneController {
    private static Stage stage;

    /**
     * This function creates a new window with the specified title, fxml file name, width, and height.
     *
     * @param title              is the title of the window to be created.
     * @param fxmlFile           is the name of the fxml file to be loaded for the window.
     */
    public static void createWindow(String title, String fxmlFile) {
        try {
            Stage stage = new Stage();
            stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResource("images/logo.png")).openStream()));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
            Pane layout = loader.load();

            Scene scene = new Scene(layout);//, width, height);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function changes the current scene of the application to the specified fxml file.
     * @param ev is the source event that triggers the scene change.
     * @param url is the name of the fxml file to be loaded for the new scene.
     * @throws IOException if the specified fxml file cannot be loaded.
     */
    public static void changeScene(ActionEvent ev, String url) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(url)));
        stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static boolean getRole(ActionEvent ev) throws IOException {
        stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
        String windoName = stage.getTitle();

        return Objects.equals(windoName, "Manager");
    }
}