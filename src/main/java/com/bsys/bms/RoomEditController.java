package com.bsys.bms;

import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import javafx.scene.control.DatePicker;

public class RoomEditController {
    @FXML private TextField roomNameTextField;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private TextField roomCapacityTextField;
    @FXML private TextField roomLocationTextBox;
    @FXML private TextArea roomDetailTextArea;
    @FXML private  DatePicker fromdate;
    @FXML private  DatePicker todate;
    @FXML private  TextField reasonTF;
    @FXML private TableView<BlockedRoom> historyView;
    @FXML private TableColumn<BlockedRoom, Date> dateFrom;
    @FXML private TableColumn<BlockedRoom, Date> dateTo;
    @FXML private TableColumn<BlockedRoom, String> reason;
    // instantiate the DatabaseController object
    final DatabaseController databaseController = new DatabaseController();
    private ObservableList<BlockedRoom> blockedData = FXCollections.observableArrayList();
    private int selectedRoomId = RoomViewController.selectedRoomId;
    Object alertType = null;
    String title = null;
    String msg = null;
    @FXML
    private void initialize() {
        roomTypeComboBox.setItems(RoomViewController.loadTypes());

        if(selectedRoomId > 0) {
            displayRoomDetails(selectedRoomId);

            // set the cell value factory for the dateFrom, dateTo, and reason columns
            dateFrom.setCellValueFactory(new PropertyValueFactory<>("datefrom"));
            dateTo.setCellValueFactory(new PropertyValueFactory<>("dateto"));
            reason.setCellValueFactory(new PropertyValueFactory<>("reason"));

            // set the items for the historyView table
            blockedData = listBlockedHistory(selectedRoomId);
            historyView.setItems(blockedData);
        }
    }

    // This method handles the "Back" button click and switches to the room view scene
    public void handleBack(ActionEvent actionEvent) throws IOException {
        // Clear the selected mode
        selectedRoomId = 0;
        // Change the scene to the room view scene
        SceneController.changeScene(actionEvent, "room-view.fxml");
    }

    public synchronized void handleSave(ActionEvent actionEvent) throws IOException {
        int err = 0;

        if(!FormValidator.validateTextField(roomNameTextField)) err ++;
        else if(!FormValidator.validateComboBox(roomTypeComboBox)) err ++;
        else if(!FormValidator.validateInteger(roomCapacityTextField)) err ++;
        else if(!FormValidator.validateTextField(roomLocationTextBox)) err ++;
        else if(!FormValidator.validateTextArea(roomDetailTextArea)) err ++;

        if(err == 0){
            // Capture the values of the fields
            String roomName = roomNameTextField.getText();
            String roomType = roomTypeComboBox.getValue();
            int roomCapacity = Integer.parseInt(roomCapacityTextField.getText());
            String roomLocation = roomLocationTextBox.getText();
            String roomDetails = roomDetailTextArea.getText();


            if(selectedRoomId > 0) {
                // update existing entry
                int result = databaseController.executeUpdateQuery("UPDATE room set name = '" + roomName + "', type = '" + roomType + "', capacity = '" + roomCapacity + "', location = '" + roomLocation + "', detail = '" + roomDetails + "' WHERE id = "+ selectedRoomId);
                alertType = (result > 0) ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
                title = (result > 0) ? "Success" : "Failure";
                msg = (result > 0) ? "The room was updated successfully." : "The room was not updated. Please try again.";
            }
            else {
                // insert new entry
                int result = databaseController.executeInsertQuery("INSERT INTO room(name, type, capacity, location, detail) VALUES ('" + roomName + "','" + roomType + "','" + roomCapacity + "','" + roomLocation + "','" + roomDetails + "')");
                alertType = (result > 0) ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
                title = (result > 0) ? "New Room Created" : "Creation Failed";
                msg = (result > 0) ? "The room was created successfully." : "The room was not created. Please try again.";
            }

            new AlertController(alertType, title, msg);
            SceneController.changeScene(actionEvent, "room-view.fxml");
        }
    }

    // Handle the room blocking function
    public synchronized void handleRoomBlock(ActionEvent ev) throws IOException {
        int err = 0;

        // Check if a room has been selected and in edit mode
        if(selectedRoomId > 0) {
            // Get the date range and reason for blocking the room
            LocalDate dateFrom = fromdate.getValue();
            LocalDate dateTo = todate.getValue();
            String reasonText = reasonTF.getText();

            if(!FormValidator.validateDatePicker(fromdate, null)) err ++;
            if(!FormValidator.validateDatePicker(todate, fromdate)) err ++;
            else if(!FormValidator.validateTextField(reasonTF)) err ++;

            if(err == 0) {
                // Check if date range and reason have been entered
                if(dateFrom != null && dateTo != null && !Objects.equals(reasonText, "")) {
                    // Reset the date range and reason fields
                    fromdate.setValue(null);
                    todate.setValue(null);
                    reasonTF.setText("");

                    // Insert the blackout data into the database
                    int result = databaseController.executeInsertQuery("INSERT INTO blackout(room_id, date_from, date_to, reason) VALUES ("+ selectedRoomId+", '"+dateFrom+"', '"+dateTo+"', '"+reasonText+"' )");

                    // Check if the insert was successful
                    if (result > 0) {
                        // Show success alert
                        alertType =  Alert.AlertType.INFORMATION;
                        title = "Success";
                        msg = "Room has been Blocked";
                        // Update the blocked history view
                        blockedData = listBlockedHistory(selectedRoomId);
                        historyView.setItems(blockedData);
                    } else {
                        // Show failure alert
                        alertType = Alert.AlertType.ERROR;
                        title = "Failure";
                        msg = "We could not block the room. Please try again.";
                    }
                }
                else {
                    // Show error alert if date range and reason are not entered
                    alertType = Alert.AlertType.ERROR;
                    title = "Failed";
                    msg = "Enter the duration and reason to block.";
                }
            }
        }
        else {
            // Show error alert if room id is missing or not in edit mode
            alertType = Alert.AlertType.ERROR;
            title = "Invalid Room Id";
            msg = "Room Id Parameter Missing";
            selectedRoomId = 0;
            SceneController.changeScene(ev, "room-view.fxml");
        }

        new AlertController(alertType, title, msg);
    }


    /**
     * listBlockedHistory() is a method that returns a list of blocked room history.
     * @param roomId is the id of the room whose blocked history is to be retrieved.
     * @return history is an ObservableList containing all the blocked history of the room.
     * The method queries the 'blackout' table in the database to retrieve the blocked room history.
     * The result set is then processed to extract the necessary information and store it in the BlockedRoom object.
     * The BlockedRoom objects are then added to the history ObservableList.
     */
    private ObservableList<BlockedRoom> listBlockedHistory(int roomId) {
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT * from blackout WHERE room_id = " + roomId);
        try {
            blockedData.clear();
            while (resultSet.next()) {
                int room_id = resultSet.getInt("room_id");
                Date date_from = resultSet.getDate("date_from");
                Date date_to = resultSet.getDate("date_to");
                String reason_str = resultSet.getString("reason");
                blockedData.add(new BlockedRoom(room_id, date_from, date_to, reason_str));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blockedData;
    }

    //This method loads the room types from the "room" table in the database and returns an ObservableList of strings representing the types
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