package com.bsys.bms;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // Override the `start` method from the Application class to setup and start the program
    @Override
    public void start(Stage primaryStage) {
        int clerk_count = 1;

        // Start a new thread for the manager window
        // This is used to create a new window for the manager interface
        Thread managerThread = new Thread(() -> Platform.runLater(() -> {
            SceneController.createWindow("Manager Window", "room-view.fxml"); // display the manager interface
        }));
        managerThread.start();

        // Start a new thread for each clerk window
        for(int i = 1; i<= clerk_count; i++) {
            int index = i;
            Thread clerkThread = new Thread(() -> Platform.runLater(() -> {
                SceneController.createWindow("Clerk " + index + " Window", "booking-view.fxml"); // display the manager interface
            }));
            clerkThread.start();
        }
    }
}