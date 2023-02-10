package com.bsys.bms;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookingController {

    @FXML
    private Label pageTitle;
    private DatabaseController databaseController = new DatabaseController();

    private void initialize() {

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
    private ObservableList<Rooms> loadBookings(String filter_param) {

        ObservableList<Rooms> data = FXCollections.observableArrayList();
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT r.*, b.*  FROM room r left join booking b on b.room_id = r.id WHERE "+ filter_param);
        try {
            //System.out.println("Result set is: " + resultSet);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String detail = resultSet.getString("detail");
                String type = resultSet.getString("type");
                String location = resultSet.getString("location");
                int capacity = resultSet.getInt("capacity");
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
