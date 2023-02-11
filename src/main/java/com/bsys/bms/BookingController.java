package com.bsys.bms;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class BookingController {

    @FXML TableView<Booking> bookingTable;

    @FXML
    private Label pageTitle;
    public static String selectedMode;
    public static int selectedId;
    private DatabaseController databaseController = new DatabaseController();

    private void initialize() {
        bookingTable.setItems(loadBookings("1"));
    }

    public void handleAddBooking(ActionEvent event) throws IOException {
        SceneController.changeScene(event, "booking-edit.fxml");
    }

    public void handleBack(ActionEvent event) throws IOException {
        SceneController.changeScene(event, "booking-view.fxml");
    }

    public void checkAvailability(ActionEvent event) throws IOException {

    }

    public void saveBooking(ActionEvent event) throws IOException {
        SceneController.changeScene(event, "booking-view.fxml");
    }
    private ObservableList<Booking> loadBookings(String filter_param) {
        ObservableList<Booking> data = FXCollections.observableArrayList();
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT r.*, b.* FROM booking b join room r on b.room_id = r.id WHERE "+ filter_param);
        try {
            //System.out.println("Result set is: " + resultSet);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int roomId = resultSet.getInt("room_id");
                String organization = resultSet.getString("organisation");
                String contactPerson = resultSet.getString("contact_person");
                String contactDetail = resultSet.getString("contact_detail");
                Date dateFrom = resultSet.getDate("date_from");
                String timeFrom = resultSet.getString("time_from");
                Date dateTo = resultSet.getDate("date_to");
                String timeTo = resultSet.getString("time_to");
                String roomName = resultSet.getString("name");
                String roomType = resultSet.getString("type");
                int roomCapacity = resultSet.getInt("capacity");
                Button action = new Button("Edit");

                // Add a new booking object to the `data` list with the retrieved data
                data.add(new Booking(id, roomId, organization, contactPerson, contactDetail, dateFrom, timeFrom, dateTo, timeTo, roomName, roomType, roomCapacity, action));

                // Add an action listener to the "Edit" button
                action.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Set the selected mode to "E" for edit mode
                        BookingController.selectedMode = "E";
                        BookingController.selectedId = id;

                        // Change the scene to the booking-edit.fxml
                        try {
                            SceneController.changeScene(event, "booking-edit.fxml");
                        } catch (IOException e) {
                            // Throw a runtime exception if the scene change fails
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
    public void handleFilters(ActionEvent actionEvent) {
    }

    public void handleReset(ActionEvent actionEvent) {
    }
}