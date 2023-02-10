package com.bsys.bms;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class RoomsDataModel {

    @FXML
    private TextField roomNameTextField;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private  TextField roomCapacityTextField;
    @FXML private  TextField roomLocationTextBox;
    @FXML private TextArea roomDetailTextArea;
    private static RoomsDataModel instance;
    private ObservableList<Rooms> roomsDataList;
    private ObservableList<String> typesOfRoomsList;

    private DatabaseController databaseController = new DatabaseController();

    private RoomsDataModel() {
        roomsDataList = FXCollections.observableArrayList();
        typesOfRoomsList = FXCollections.observableArrayList();
        SelectDataFromRooms("1");
        getTypesOfRooms();
    }

    public static RoomsDataModel getInstance() {
        if (instance == null) {
            instance = new RoomsDataModel();
        }
        return instance;
    }

    public ObservableList<Rooms> SelectDataFromRooms(String filter_param) {
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT room.*, COUNT(booking.room_id) as active_booking FROM room LEFT JOIN booking ON room.id = booking.room_id WHERE "+filter_param+" GROUP BY room.id");
        try {
            int a = 1;
            while (resultSet.next()) {
                int selectedId = resultSet.getInt("id");
                String roomName = resultSet.getString("name");
                int roomCapacity = resultSet.getInt("capacity");
                String roomDetail = resultSet.getString("detail");
                String roomType = resultSet.getString("type");
                int activeBookings = resultSet.getInt("active_booking");
                Button action = new Button("Edit");
                roomsDataList.add(new Rooms(a, roomName, roomCapacity, roomDetail, roomType, activeBookings, action));
                a++;

                action.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        RoomController.selectedMode = "E";
                        RoomController.selectedRoomId = selectedId;
                        try {
                            SceneController.changeScene(event, "room-edit.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomsDataList;
    }

    public ObservableList<String> getTypesOfRooms() {
        ObservableList<String> data = FXCollections.observableArrayList();
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT distinct(type) as type FROM room");
        try {
            while (resultSet.next()) {
                String name = resultSet.getString("type");
                data.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void getRoomDetails(int roomId) {
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT * FROM room WHERE id = " + roomId);
        try {
            if (resultSet.next()) {
                String roomName = resultSet.getString("name");
                int roomCapacity = resultSet.getInt("capacity");
                String roomDetail = resultSet.getString("detail");
                String roomType = resultSet.getString("type");
                String roomLocation = resultSet.getString("location");

                roomNameTextField.setText(roomName);
                roomCapacityTextField.setText(String.valueOf(roomCapacity));
                roomDetailTextArea.setText(roomDetail);
                roomTypeComboBox.getSelectionModel().select(roomType);
                roomLocationTextBox.setText(String.valueOf(roomLocation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
