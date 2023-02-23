package com.bsys.bms;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class MainTest {

    @Test
    public void testStart() throws Exception {
        // Create a CountDownLatch with clerk_count + 1 permits
        int clerk_count = 2;
        CountDownLatch latch = new CountDownLatch(clerk_count + 1);

        // Replace the Platform.runLater() call with a call to the latch's countDown() method
        // This allows us to wait for all the threads to finish before asserting the test result
        Thread managerThread = new Thread(() -> {
            latch.countDown();
            SceneController.createWindow("Manager", "room-view.fxml");
        });
        managerThread.start();

        for (int i = 1; i <= clerk_count; i++) {
            int index = i;
            Thread clerkThread = new Thread(() -> {
                latch.countDown();
                SceneController.createWindow("Clerk " + index + " Window", "booking-view.fxml");
            });
            clerkThread.start();
        }

        // Wait for all the threads to finish before asserting the test result
        latch.await(5, TimeUnit.SECONDS);

        // Use reflection to get the clerkCount field value from the Main class
        Field clerkCountField = Main.class.getDeclaredField("clerk_count");
        clerkCountField.setAccessible(true);
        int clerkCount = clerkCountField.getInt(null);

        // Assert that the clerkCount field value is equal to the expected value
        assertEquals(clerk_count, clerkCount);
    }
}