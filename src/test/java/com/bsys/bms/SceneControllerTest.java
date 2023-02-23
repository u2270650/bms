package com.bsys.bms;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SceneControllerTest {
    private Stage stage;

    @BeforeEach
    public void setUp() throws Exception {
        Platform.startup(() -> {
            stage = new Stage();
        });
    }

    @AfterEach
    public void tearDown() throws Exception {
        Platform.runLater(() -> {
            if (stage != null) {
                stage.close();
            }
        });
    }

    @Test
    public void testCreateWindow() throws Exception {
        String title = "Test Window";
        String fxmlFile = "/test.fxml";

        SceneController.createWindow(title, fxmlFile);

        Platform.runLater(() -> {
            assertNotNull(stage);
            assertNotNull(stage.getScene());
            assertNotNull(stage.getIcons());
            stage.close();
        });
    }

    @Test
    public void testChangeScene() throws Exception {
        String url = "room-view.fxml";

        Platform.runLater(() -> {
            try {
                SceneController.changeScene(new ActionEvent() {}, url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            assertNotNull(stage);
            assertNotNull(stage.getScene());
            stage.close();
        });
    }


    @Test
    public void testGetRole() throws Exception {
        Platform.runLater(() -> {
            stage = new Stage();
            stage.setTitle("Manager");
        });

        boolean isManager = SceneController.getRole(new ActionEvent());

        Platform.runLater(() -> {
            assertNotNull(stage);
            stage.close();
        });

        assertTrue(isManager);
    }
}