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

    private DatabaseController databaseController = new DatabaseController();
    @FXML
    private void initialize() {
        if(RoomController.selectedRoomId > 0 && Objects.equals(RoomController.selectedMode, "E")) {
            // edit mode
            roomTypeComboBox.setItems(loadTypes());
            displayRoomDetails(RoomController.selectedRoomId);

            dateFrom.setCellValueFactory(new PropertyValueFactory<>("datefrom"));
            dateTo.setCellValueFactory(new PropertyValueFactory<>("dateto"));
            reason.setCellValueFactory(new PropertyValueFactory<>("reason"));
            historyView.setItems(listBlockedHistory(RoomController.selectedRoomId));
        }
        else if(Objects.equals(RoomController.selectedMode, "A")) {
            // add mode
            roomTypeComboBox.setItems(loadTypes());
        }
        else {
            // display mode
            cmbtype.setItems(loadTypes());
            sr_num.setCellValueFactory(new PropertyValueFactory<>("sr_num"));
            room_name.setCellValueFactory(new PropertyValueFactory<>("room_name"));
            room_capacity.setCellValueFactory(new PropertyValueFactory<>("room_capacity"));
            room_detail.setCellValueFactory(new PropertyValueFactory<>("room_detail"));
            room_type.setCellValueFactory(new PropertyValueFactory<>("room_type"));
            active_bookings.setCellValueFactory(new PropertyValueFactory<>("active_bookings"));
            action.setCellValueFactory(new PropertyValueFactory<>("action"));
            tableView.setItems(loadRooms("1"));
        }
    }
    public void handleFilters(ActionEvent actionEvent) {
        String keyword = txtkeyword.getText();
        String room_type = (!cmbtype.getSelectionModel().isEmpty()) ? cmbtype.getSelectionModel().getSelectedItem().toLowerCase() : "";
        String param_str = "1";

        param_str += (!room_type.isEmpty()) ? " AND room.type ='"+room_type+"'" : "";
        param_str += (!keyword.isEmpty()) ? " AND (room.name LIKE '%"+keyword+"%' OR room.detail LIKE '%"+keyword+"%' )" : "";

        tableView.setItems(loadRooms(param_str));
    }

    public void handleReset(ActionEvent actionEvent) {
        txtkeyword.setText("");
        cmbtype.setValue("");
        String param_str = "1";

        tableView.setItems(loadRooms(param_str));
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
            int result = databaseController.executeInsertQuery("INSERT INTO room(name, type, capacity, location, detail) VALUES ('" + roomName + "','" + roomType + "','" + roomCapacity + "','" + roomLocation + "','" + roomDetails + "')");

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

    public void handleRoomBlock(ActionEvent ev) throws IOException {
        if(RoomController.selectedRoomId > 0 && Objects.equals(RoomController.selectedMode, "E")) {
            LocalDate dateFrom = fromdate.getValue();
            LocalDate dateTo = todate.getValue();
            String reasonText = reasonTF.getText();

            if(dateFrom != null && dateTo != null && !Objects.equals(reasonText, "")) {
                fromdate.setValue(null);
                todate.setValue(null);
                reasonTF.setText("");

                int result = databaseController.executeInsertQuery("INSERT INTO blackout(room_id, date_from, date_to, reason) VALUES ("+RoomController.selectedRoomId+", '"+dateFrom+"', '"+dateTo+"', '"+reasonText+"' )");
                if (result > 0) {
                    // Alert user of successful insert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Room has been Blocked");
                    alert.showAndWait();
                    historyView.setItems(listBlockedHistory(RoomController.selectedRoomId));
                } else {
                    // Alert user of failed insert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failure");
                    alert.setHeaderText("We could not block the room. Please try again.");
                    RoomController.selectedRoomId = 0;
                    RoomController.selectedMode = "";
                    alert.showAndWait();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText("Enter the duration and reason to block.");
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Room Id");
            alert.setHeaderText("Room Id Parameter Missing");
            RoomController.selectedRoomId = 0;
            RoomController.selectedMode = "";
            alert.showAndWait();
            SceneController.changeScene(ev, "room-view.fxml");
        }
    }


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
                data.add(new Rooms(a, roomName, roomCapacity, roomDetail, roomType, activeBookings, action));
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

        return data;
    }
    private ObservableList<String> loadTypes() {
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

    private void displayRoomDetails(int roomId) {
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