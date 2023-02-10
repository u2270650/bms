package com.bsys.bms;

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
import java.util.Date;
import java.util.Objects;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;

public class RoomController {
    private static int selectedRoomId;
    private static String selectedMode;
    @FXML private TextField txtkeyword;
    @FXML private TableView<Rooms> tableView;
    @FXML private TableColumn<Rooms, Integer> sr_num;
    @FXML private TableColumn<Rooms, String> room_name;
    @FXML private TableColumn<Rooms, Integer> room_capacity;
    @FXML private TableColumn<Rooms, String> room_detail;
    @FXML private TableColumn<Rooms, String> room_type;
    @FXML private TableColumn<Rooms, Integer> active_bookings;
    @FXML private TableColumn<Rooms, String> action;
    @FXML private ComboBox<String> cmbtype;

    @FXML private TextField roomNameTextField;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private  TextField roomCapacityTextField;
    @FXML private  TextField roomLocationTextBox;
    @FXML private  TextArea roomDetailTextArea;

    @FXML private TableView<BlockedRoom> historyView;
    @FXML private TableColumn<BlockedRoom, Date> dateFrom;
    @FXML private TableColumn<BlockedRoom, Date> dateTo;
    @FXML private TableColumn<BlockedRoom, String> reason;
    @FXML private  DatePicker fromdate;
    @FXML private  DatePicker todate;
    @FXML private  TextField reasonTF;
    // instantiate the DatabaseController object
    private DatabaseController databaseController = new DatabaseController();
    @FXML
    private void initialize() {
        // check if the room id is greater than 0 and selected mode is "E"
        if(RoomController.selectedRoomId > 0 && Objects.equals(RoomController.selectedMode, "E")) {
            // in edit mode
            roomTypeComboBox.setItems(loadTypes()); // set the items in the roomTypeComboBox
            // display room details for the selected room
            displayRoomDetails(RoomController.selectedRoomId);

            // set the cell value factory for the dateFrom, dateTo, and reason columns
            dateFrom.setCellValueFactory(new PropertyValueFactory<>("datefrom"));
            dateTo.setCellValueFactory(new PropertyValueFactory<>("dateto"));
            reason.setCellValueFactory(new PropertyValueFactory<>("reason"));

            // set the items for the historyView table
            historyView.setItems(listBlockedHistory(RoomController.selectedRoomId));
        }
        else if(Objects.equals(RoomController.selectedMode, "A")) {
            // in add mode
            roomTypeComboBox.setItems(loadTypes()); // set the items in the roomTypeComboBox
        }
        else {
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

            // set the items for the tableView table
            tableView.setItems(loadRooms("1"));
        }
    }

    // This method filters the room data based on the selected parameters
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

    // This method resets the filters and loads all the rooms
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
        // Set the selected mode to "Add"
        RoomController.selectedMode = "A";
        // Change the scene to the add room scene
        SceneController.changeScene(actionEvent, "room-edit.fxml");
    }

    // This method handles the "Back" button click and switches to the room view scene
    public void handleBack(ActionEvent actionEvent) throws IOException {
        // Clear the selected mode
        RoomController.selectedMode = "";
        // Change the scene to the room view scene
        SceneController.changeScene(actionEvent, "room-view.fxml");
    }

    public void handleSave(ActionEvent actionEvent) throws IOException {
        // Capture the values of the fields
        String roomName = roomNameTextField.getText();
        String roomType = (String) roomTypeComboBox.getValue();
        String roomCapacity = roomCapacityTextField.getText();
        String roomLocation = roomLocationTextBox.getText();
        String roomDetails = roomDetailTextArea.getText();

        if(RoomController.selectedRoomId > 0 && Objects.equals(RoomController.selectedMode, "E")) {
            // update existing entry
            int result = databaseController.executeUpdateQuery("UPDATE room set name = '" + roomName + "', type = '" + roomType + "', capacity = '" + roomCapacity + "', location = '" + roomLocation + "', detail = '" + roomDetails + "' WHERE id = "+RoomController.selectedRoomId);

            if (result > 0) {
                // Alert user of successful insert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("The room was updated successfully.");
                RoomController.selectedRoomId = 0;
                RoomController.selectedMode = "";
                alert.showAndWait();
            } else {
                // Alert user of failed insert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failure");
                alert.setHeaderText("The room was not updated. Please try again.");
                RoomController.selectedRoomId = 0;
                RoomController.selectedMode = "";
                alert.showAndWait();
            }
        }
        else {
            // insert new entry
            int result = databaseController.executeInsertQuery("INSERT INTO room(name, type, capacity, location, detail) VALUES ('" + roomName + "','" + roomType + "','" + roomCapacity + "','" + roomLocation + "','" + roomDetails + "')");

            if (result > 0) {
                // Alert user of successful insert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("New Room Created");
                alert.setHeaderText("The room was created successfully.");
                RoomController.selectedRoomId = 0;
                RoomController.selectedMode = "";
                alert.showAndWait();
            } else {
                // Alert user of failed insert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Creation Failed");
                alert.setHeaderText("The room was not created. Please try again.");
                RoomController.selectedRoomId = 0;
                RoomController.selectedMode = "";
                alert.showAndWait();
            }
        }
        SceneController.changeScene(actionEvent, "room-view.fxml");
    }

    // Handle the room blocking function
    public void handleRoomBlock(ActionEvent ev) throws IOException {
        // Check if a room has been selected and in edit mode
        if(RoomController.selectedRoomId > 0 && Objects.equals(RoomController.selectedMode, "E")) {
            // Get the date range and reason for blocking the room
            LocalDate dateFrom = fromdate.getValue();
            LocalDate dateTo = todate.getValue();
            String reasonText = reasonTF.getText();

            // Check if date range and reason have been entered
            if(dateFrom != null && dateTo != null && !Objects.equals(reasonText, "")) {
                // Reset the date range and reason fields
                fromdate.setValue(null);
                todate.setValue(null);
                reasonTF.setText("");

                // Insert the blackout data into the database
                int result = databaseController.executeInsertQuery("INSERT INTO blackout(room_id, date_from, date_to, reason) VALUES ("+RoomController.selectedRoomId+", '"+dateFrom+"', '"+dateTo+"', '"+reasonText+"' )");

                // Check if the insert was successful
                if (result > 0) {
                    // Show success alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Room has been Blocked");
                    alert.showAndWait();
                    // Update the blocked history view
                    historyView.setItems(listBlockedHistory(RoomController.selectedRoomId));
                } else {
                    // Show failure alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failure");
                    alert.setHeaderText("We could not block the room. Please try again.");
                    RoomController.selectedRoomId = 0;
                    RoomController.selectedMode = "";
                    alert.showAndWait();
                }
            }
            else {
                // Show error alert if date range and reason are not entered
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText("Enter the duration and reason to block.");
            }
        }
        else {
            // Show error alert if room id is missing or not in edit mode
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Room Id");
            alert.setHeaderText("Room Id Parameter Missing");
            RoomController.selectedRoomId = 0;
            RoomController.selectedMode = "";
            alert.showAndWait();
            SceneController.changeScene(ev, "room-view.fxml");
        }
    }


    /**
     * listBlockedHistory() is a method that returns a list of blocked room history.
     * @param roomId is the id of the room whose blocked history is to be retrieved.
     * @return history is an ObservableList containing all the blocked history of the room.
     *
     * The method queries the 'blackout' table in the database to retrieve the blocked room history.
     * The result set is then processed to extract the necessary information and store it in the BlockedRoom object.
     * The BlockedRoom objects are then added to the history ObservableList.
     */
    private ObservableList<BlockedRoom> listBlockedHistory(int roomId) {
        ObservableList<BlockedRoom> history = FXCollections.observableArrayList();
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT * from blackout WHERE room_id = "+roomId);

        try {
            while (resultSet.next()) {
                int room_id = resultSet.getInt("room_id");
                Date date_from = resultSet.getDate("date_from");
                Date date_to = resultSet.getDate("date_to");
                String reason_str = resultSet.getString("reason");
                history.add(new BlockedRoom(room_id, date_from, date_to, reason_str));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    private ObservableList<Rooms> loadRooms(String filter_param) {

        ObservableList<Rooms> data = FXCollections.observableArrayList();

        // Execute a SELECT query to retrieve the data for the rooms
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT room.*, COUNT(booking.room_id) as active_booking FROM room LEFT JOIN booking ON room.id = booking.room_id WHERE "+filter_param+" GROUP BY room.id");
        try {
            int a = 1;
            // Iterate through the result set and retrieve the room data
            while (resultSet.next()) {
                int selectedId = resultSet.getInt("id");
                String roomName = resultSet.getString("name");
                int roomCapacity = resultSet.getInt("capacity");
                String roomDetail = resultSet.getString("detail");
                String roomType = resultSet.getString("type");
                int activeBookings = resultSet.getInt("active_booking");
                Button action = new Button("Edit");

                // Add a new room object to the `data` list with the retrieved data
                data.add(new Rooms(a, roomName, roomCapacity, roomDetail, roomType, activeBookings, action));
                a++;

                // Add an action listener to the "Edit" button
                action.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Set the selected mode to "E" for edit mode
                        RoomController.selectedMode = "E";
                        RoomController.selectedRoomId = selectedId;

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
        } catch (SQLException e) {
            // Print the stack trace of the exception if there is an error in the database query
            e.printStackTrace();
        }

        // Return the `data` list of room objects
        return data;
    }

    //This method loads the room types from the "room" table in the database and returns an ObservableList of strings representing the types
    private ObservableList<String> loadTypes() {
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

    // This method is used to display the details of a room in the text fields and combo box
    private void displayRoomDetails(int roomId) {
        // Execute the SELECT query to get the room details from the database
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT * FROM room WHERE id = " + roomId);
        try {
            // Check if there is a result from the query
            if (resultSet.next()) {
                // Get the values of the columns in the result
                String roomName = resultSet.getString("name");
                int roomCapacity = resultSet.getInt("capacity");
                String roomDetail = resultSet.getString("detail");
                String roomType = resultSet.getString("type");
                String roomLocation = resultSet.getString("location");

                // Set the values of the text fields and combo box to the values from the query result
                roomNameTextField.setText(roomName);
                roomCapacityTextField.setText(String.valueOf(roomCapacity));
                roomDetailTextArea.setText(roomDetail);
                roomTypeComboBox.getSelectionModel().select(roomType);
                roomLocationTextBox.setText(String.valueOf(roomLocation));
            }
        } catch (SQLException e) {
            // If there is an error, print the stack trace
            e.printStackTrace();
        }
    }
}