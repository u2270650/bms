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
import java.util.Objects;

public class RoomController {
    static int selectedRoomId;
    static String selectedMode;
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

    private DatabaseController databaseController = new DatabaseController();
    @FXML
    private void initialize() {
        if(RoomController.selectedRoomId > 0 && Objects.equals(RoomController.selectedMode, "E")) {
            // edit mode
            roomTypeComboBox.setItems(RoomsDataModel.getInstance().getTypesOfRooms());
            RoomsDataModel.getInstance().getRoomDetails(RoomController.selectedRoomId);
        }
        else if(Objects.equals(RoomController.selectedMode, "A")) {
            // add mode
            roomTypeComboBox.setItems(RoomsDataModel.getInstance().getTypesOfRooms());
        }
        else {
            // display mode
            cmbtype.setItems(RoomsDataModel.getInstance().getTypesOfRooms());
            sr_num.setCellValueFactory(new PropertyValueFactory<>("sr_num"));
            room_name.setCellValueFactory(new PropertyValueFactory<>("room_name"));
            room_capacity.setCellValueFactory(new PropertyValueFactory<>("room_capacity"));
            room_detail.setCellValueFactory(new PropertyValueFactory<>("room_detail"));
            room_type.setCellValueFactory(new PropertyValueFactory<>("room_type"));
            active_bookings.setCellValueFactory(new PropertyValueFactory<>("active_bookings"));
            action.setCellValueFactory(new PropertyValueFactory<>("action"));
            tableView.setItems(RoomsDataModel.getInstance().SelectDataFromRooms("1"));
        }
    }
    public void handleFilters(ActionEvent actionEvent) {
        String keyword = txtkeyword.getText();
        String room_type = (!cmbtype.getSelectionModel().isEmpty()) ? cmbtype.getSelectionModel().getSelectedItem().toLowerCase() : "";
        String param_str = "1";

        param_str += (!room_type.isEmpty()) ? " AND room.type ='"+room_type+"'" : "";
        param_str += (!keyword.isEmpty()) ? " AND (room.name LIKE '%"+keyword+"%' OR room.detail LIKE '%"+keyword+"%' )" : "";

        tableView.setItems(RoomsDataModel.getInstance().SelectDataFromRooms(param_str));
    }

    public void handleReset(ActionEvent actionEvent) {
        txtkeyword.setText("");
        cmbtype.setValue("");
        String param_str = "1";

        tableView.setItems(RoomsDataModel.getInstance().SelectDataFromRooms(param_str));
    }

    public void handleAddRoom(ActionEvent actionEvent) throws IOException {
        RoomController.selectedMode = "A";
        SceneController.changeScene(actionEvent, "room-edit.fxml");
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        RoomController.selectedMode = "";
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
                RoomController.selectedRoomId = result;
                RoomController.selectedMode = "E";
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
            int result = databaseController.executeInsertQuery("INSERT INTO room(name, type, capacity, location, detail) VALUES ('" + roomName + "','" + roomType + "', " + roomCapacity + ",'" + roomLocation + "','" + roomDetails + "')");

            if (result > 0) {
                // Alert user of successful insert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("New Room Created");
                alert.setHeaderText("The room was created successfully.");
                RoomController.selectedRoomId = result;
                RoomController.selectedMode = "E";
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

        SceneController.changeScene(actionEvent, "room-edit.fxml");
    }
}