package com.bsys.bms;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javafx.scene.control.DatePicker;

public class RoomViewController {
    public static int selectedRoomId;
    private static int sr_counter =  1;
    @FXML private TextField txtkeyword;
    @FXML private ComboBox<String> cmbtype;
    @FXML private TableView<Rooms> tableView;
    @FXML private TableColumn<Rooms, Integer> sr_num;
    @FXML private TableColumn<Rooms, String> room_name;
    @FXML private TableColumn<Rooms, Integer> room_capacity;
    @FXML private TableColumn<Rooms, String> room_detail;
    @FXML private TableColumn<Rooms, String> room_type;
    @FXML private TableColumn<Rooms, Integer> active_bookings;
    @FXML private TableColumn<Rooms, String> action;
    private static DatabaseController databaseController = new DatabaseController();
    ObservableList<Rooms> roomsData = FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        // in display mode
        cmbtype.setItems(loadTypes()); // set the items in the cmbtype ComboBox

        // set the cell value factory for the sr_num, room_name, room_capacity, room_detail, room_type, active_bookings, and action columns
        sr_num.setCellValueFactory(new PropertyValueFactory<>("sr_num"));
        room_name.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        room_capacity.setCellValueFactory(new PropertyValueFactory<>("room_capacity"));
        room_detail.setCellValueFactory(new PropertyValueFactory<>("room_detail"));
        room_type.setCellValueFactory(new PropertyValueFactory<>("room_type"));
        active_bookings.setCellValueFactory(new PropertyValueFactory<>("active_bookings"));
        action.setCellValueFactory(new PropertyValueFactory<>("action"));
        roomsData.addListener(new ListChangeListener<Rooms>() {
            @Override
            public void onChanged(Change<? extends Rooms> change) {
                // set the items for the tableView table
                // System.err.println("data change logged...");
            }
        });

        tableView.setItems(loadRooms("1"));
    }

    public void handleFilters(ActionEvent actionEvent) {
        // Get the keyword for the filter
        String keyword = txtkeyword.getText();
        // Get the selected room type for the filter
        String room_type = (!cmbtype.getSelectionModel().isEmpty()) ? cmbtype.getSelectionModel().getSelectedItem().toLowerCase() : "";
        // Initialize the parameter string for the filter
        String param_str = "1";

        // Check if room type is selected and add it to the parameter string
        param_str += (!room_type.isEmpty()) ? " AND room.type ='"+room_type+"'" : "";
        // Check if keyword is not empty and add it to the parameter string
        param_str += (!keyword.isEmpty()) ? " AND (room.name LIKE '%"+keyword+"%' OR room.detail LIKE '%"+keyword+"%' )" : "";

        // Load the filtered room data based on the parameters
        tableView.setItems(loadRooms(param_str));
    }


    public void handleReset(ActionEvent actionEvent) {
        // Clear the keyword filter
        txtkeyword.setText("");
        // Clear the selected room type filter
        cmbtype.setValue("");
        // Initialize the parameter string to load all rooms
        String param_str = "1";

        // Load all the rooms
        tableView.setItems(loadRooms(param_str));
    }

    // This method handles the "Add Room" button click and switches to the add room scene
    public void handleAddRoom(ActionEvent actionEvent) throws IOException {
        // Change the scene to the add room scene
        selectedRoomId = 0;
        SceneController.changeScene(actionEvent, "room-edit.fxml");
    }

    private void editButton(Button action, int selectedId) {
        action.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Set the selected mode to "E" for edit mode
                selectedRoomId = selectedId;

                //System.err.println("selectedRoomId: "+selectedRoomId);

                // Change the scene to the room-edit.fxml
                try {
                    SceneController.changeScene(event, "room-edit.fxml");
                } catch (IOException e) {
                    // Throw a runtime exception if the scene change fails
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private ObservableList<Rooms> loadRooms(String filter_param) {
        // Execute a SELECT query to retrieve the data for the rooms
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT room.*, COUNT(booking.room_id) as active_booking FROM room LEFT JOIN booking ON room.id = booking.room_id WHERE "+filter_param+" GROUP BY room.id");
        try {
            roomsData.clear();
            int a = 1;
            // Iterate through the result set and retrieve the room data
            while (resultSet.next()) {
                int selectedId = resultSet.getInt("id");
                String roomName = resultSet.getString("name");
                int roomCapacity = resultSet.getInt("capacity");
                String roomDetail = resultSet.getString("detail");
                String roomType = resultSet.getString("type");
                int activeBookings = resultSet.getInt("active_booking");
                String location = resultSet.getString("location");
                Button action = new Button("Edit");

                // Add a new room object to the `data` list with the retrieved data
                roomsData.add(new Rooms(a, roomName, roomCapacity, roomDetail, roomType, location, activeBookings, action));
                a++;

                // Add an action listener to the "Edit" button
                editButton(action, selectedId);
            }
            sr_counter = a;
        } catch (SQLException e) {
            // Print the stack trace of the exception if there is an error in the database query
            e.printStackTrace();
        }

        // Return the `data` list of room objects
        return roomsData;
    }

    public static ObservableList<String> loadTypes() {
        // Create an ObservableList to store the room types
        ObservableList<String> data = FXCollections.observableArrayList();

        // Execute a SELECT query to get the distinct room types from the "room" table
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT distinct(type) as type FROM room");
        try {
            // Iterate through the result set and add each room type to the ObservableList
            while (resultSet.next()) {
                String name = resultSet.getString("type");
                data.add(name);
            }
        } catch (SQLException e) {
            // Print the stack trace in case of an exception
            e.printStackTrace();
        }

        // Return the ObservableList
        return data;
    }
}
