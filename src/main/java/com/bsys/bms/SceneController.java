package com.bsys.bms;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneController {
    private static Parent root;
    private static Scene scene;
    private static Stage stage;

    public static void createWindow(String title, String fxmlFile, int width, int height) {
        try {
            Stage stage = new Stage();
            stage.getIcons().add(new Image(Main.class.getResource("images/logo.png").openStream()));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
            Pane layout = loader.load();

            Scene scene = new Scene(layout, width, height);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void changeScene(Event ev, String url) throws IOException {
        root = FXMLLoader.load(Main.class.getResource(url));
        stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}