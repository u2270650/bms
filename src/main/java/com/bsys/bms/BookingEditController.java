package com.bsys.bms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

/**
The following code is a Java class named BookingEditController. This class represents the controller of a graphical user interface (GUI) for booking management. The controller manages events that occur in the GUI, such as button clicks and updates to user inputs. The GUI is built using the JavaFX framework and the Scene Builder tool. The code performs the following tasks:

It initializes the GUI with certain default settings, such as loading types of rooms, setting the current date, and setting up time selection menus.
It handles button clicks for the Back, Reset, and Check Availability buttons.
It implements methods for checking the availability of rooms and for displaying a table of available rooms.
It allows the user to book a room.
* */
public class BookingEditController {
    // Instance variables representing various elements in the GUI
    @FXML Label messageLabel;
    @FXML TableView<Rooms> avlTable;
    @FXML TableColumn<Rooms, Integer> srNum;
    @FXML TableColumn<Rooms, String> colRoomName;
    @FXML TableColumn<Rooms, Integer> colRoomCapacity;
    @FXML TableColumn<Rooms, String> colRoomDetail;
    @FXML TableColumn<Rooms, String> colRoomType;
    @FXML TableColumn<Rooms, String> colRoomLocation;
    @FXML TableColumn<Rooms, Button> colAction;
    @FXML ComboBox<String> roomType;
    @FXML DatePicker dateFrom;
    @FXML DatePicker dateTo;
    @FXML ComboBox<String> timeFrom;
    @FXML ComboBox<String> timeTo;
    // Instance variable representing a database controller
    final static DatabaseController databaseController = new DatabaseController();
    // Class-level variables representing the selected room ID and search parameters
    public static int selectedRoomId = 0;
    public static String paramStr = "";
    public static String paramStr2 = "";
    // List of available rooms to be displayed in the GUI
    ObservableList<Rooms> roomsData = FXCollections.observableArrayList();

    // Method called when the GUI is initialized
    @FXML
    private void initialize() throws SQLException {
        // Load the types of rooms and populate the room type selection menu
        roomType.setItems(RoomViewController.loadTypes());
        // Set the date selection menu to the current date
        dateFrom.setValue(LocalDate.now());
        // Populate the time selection menus
        timeFrom.getItems().addAll("AM", "PM");
        timeTo.getItems().addAll("AM", "PM");

        // If search parameters have been set, check the availability of rooms
        if (!Objects.equals(paramStr, "")) {
            ResultSet res = get_availability(paramStr, paramStr2);
            if (!res.next())
                messageLabel.setText("No rooms available for the search criteria");
            else
                display_availability(res);
        }
    }

    // Event handler for the Back button
    @FXML
    private void handleBack(ActionEvent ev) throws IOException {
        // Change the scene to the booking-view.fxml
        SceneController.changeScene(ev, "booking-view.fxml");
    }

    // Event handler for the Reset button
    @FXML
    private void handleReset() {
        // Reset the date selection menus to the current date and clear the room type selection menu
        dateFrom.setValue(LocalDate.now());
        dateTo.setValue(null);
        // Reset the time selection menus
        timeFrom.getItems().addAll("AM", "PM");
        timeTo.getItems().addAll("AM", "PM");
        roomType.getItems().clear();
        roomType.setItems(RoomViewController.loadTypes());
        // Clear the table of available rooms
        avlTable.getItems().clear();
        roomsData.clear();
        avlTable.setItems(roomsData);
    }

    /** This method is an event handler for the Check Availability button. It is called when the button is clicked.
     It retrieves the selected dates, times, and room type from the UI controls and constructs SQL query parameters based on the selected values.
    It then calls a method named "get_availability" with the constructed query parameters to retrieve available rooms from the database.
    If no rooms are available, it displays a message to the user; otherwise, it calls a method named "display_availability" to display the available rooms to the user.
    */
    @FXML
    private synchronized void checkAvailability() throws SQLException {
        // Get selected dates, times, and room type
        LocalDate date_from = dateFrom.getValue();
        LocalDate date_to = dateTo.getValue();
        String time_from = (!timeFrom.getSelectionModel().isEmpty()) ? timeFrom.getSelectionModel().getSelectedItem().toLowerCase() : "";
        String time_to = (!timeTo.getSelectionModel().isEmpty()) ? timeTo.getSelectionModel().getSelectedItem().toLowerCase() : "";
        String room_type = (!roomType.getSelectionModel().isEmpty()) ? roomType.getSelectionModel().getSelectedItem().toLowerCase() : "";
        // Construct SQL query parameters
        String param_str2 = "1";
        String param_str = "";
        param_str += (!room_type.equals("")) ? " AND r.type = '"+room_type+"' " : "";

        param_str2 += (date_from != null) ? " AND b.date_from = '"+date_from+"' " : "";
        param_str2 += (!time_from.equals("")) ? " AND b.time_from = '"+time_from+"' " : "";
        param_str2 += (date_to != null) ? " AND b.date_to = '"+date_to+"' " : "";
        param_str2 += (!time_to.equals("")) ? " AND b.time_to = '"+time_to+"' " : "";

        // Store the constructed query parameters
        paramStr = param_str;
        paramStr2 = param_str2;

        // Call the "get_availability" method with the constructed query parameters
        ResultSet res = get_availability(param_str, param_str2);

        // If no rooms are available, display a message to the user; otherwise, display the available rooms
        if(!res.next())
            messageLabel.setText("No rooms available for the search criteria");
        else {
            display_availability(res);
        }
    }
    /**
     * Returns a ResultSet containing the available rooms based on the given search parameters.
     * @param roomTypeSql the SQL string for filtering room types
     * @param bookingSql the SQL string for filtering unavailable rooms
     * @return a ResultSet containing the available rooms
     */
    public static ResultSet get_availability(String roomTypeSql, String bookingSql) {
        // construct the SQL query to fetch the available rooms
        String query = "SELECT r.* FROM room r WHERE r.id NOT IN (SELECT b.room_id FROM booking b WHERE " + bookingSql + ") " + roomTypeSql;

        // print the constructed query for debugging purposes
        // System.err.println("Query: " + query);

        // execute the query and return the result set
        return databaseController.executeSelectQuery(query);
    }


    /**

     Updates the UI table with the results of a room availability query

     @param resultSet the ResultSet object containing the query results

     @throws RuntimeException if a SQLException is thrown while processing the ResultSet
     */
    private void display_availability(ResultSet resultSet) {
        try {
            roomsData.clear();
            int a = 1;
            while (resultSet.next()) {
                int selectedId = resultSet.getInt("id");
                String roomName = resultSet.getString("name");
                int roomCapacity = resultSet.getInt("capacity");
                String roomDetail = resultSet.getString("detail");
                String roomType = resultSet.getString("type");
                int activeBookings = 0;
                String location = resultSet.getString("location");
                Button action = new Button("Book Now");

                Rooms newRoom = new Rooms(a,roomName, roomCapacity, roomDetail, roomType, location, activeBookings, action);
                roomsData.add(newRoom);
                bookButton(action, selectedId);

                // Set cell value factories for the UI table columns
                srNum.setCellValueFactory(new PropertyValueFactory<>("sr_num"));
                colRoomName.setCellValueFactory(new PropertyValueFactory<>("room_name"));
                colRoomCapacity.setCellValueFactory(new PropertyValueFactory<>("room_capacity"));
                colRoomDetail.setCellValueFactory(new PropertyValueFactory<>("room_detail"));
                colRoomType.setCellValueFactory(new PropertyValueFactory<>("room_type"));
                colRoomLocation.setCellValueFactory(new PropertyValueFactory<>("room_location"));
                colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

                // Update the UI table with the updated data
                avlTable.setItems(roomsData);

                a ++;
            }
        } catch (SQLException e) {
            // Rethrow the SQLException as a RuntimeException to simplify error handling
            throw new RuntimeException(e);
        }
    }

    /**

     Sets an action event for a button to set the selected room ID and change to the new booking scene.
     @param action The button to add the action event to
     @param selectedId The ID of the selected room to book
     */
    private void bookButton(Button action, int selectedId) {
        action.setOnAction(event -> {
            selectedRoomId = selectedId;
            try {
                SceneController.changeScene(event, "newbooking.fxml");
            } catch (IOException e) {
                // Throw a runtime exception if the scene change fails
                throw new RuntimeException(e);
            }
        });
    }
}
