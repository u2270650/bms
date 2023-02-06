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
        int w = 1000; int h = 600; // static width and height for the window
        int clerk_count = 2;
        SceneController.createWindow("Manager Window", "manager-view.fxml",w, h); // display the manager interface

        /*for(int i = 1; i<= clerk_count; i++) {
            SceneController.createWindow("Clerk "+i+" Window", "clerk-view.fxml", 2, w, h); // display the manager interface
        }*/
    }
}
