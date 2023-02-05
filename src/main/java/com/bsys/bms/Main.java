package com.bsys.bms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Main.class.getResource("images/logo.png").openStream()));
        String fxmlFile = "manager-view.fxml";
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
        Pane layout = loader.load();

        Scene scene = new Scene(layout, 300, 300);
        stage.setScene(scene);
        stage.setTitle("Hello World!");
        stage.show();
    }
}
