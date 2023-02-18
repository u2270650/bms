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

public class BookingEditController {
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
    final static DatabaseController databaseController = new DatabaseController();
    public static int selectedRoomId = 0;
    public static String paramStr = "";
    public static String paramStr2 = "";
    ObservableList<Rooms> roomsData = FXCollections.observableArrayList();
    @FXML
    private void initialize() throws SQLException {
        roomType.setItems(RoomViewController.loadTypes());
        dateFrom.setValue(LocalDate.now());
        timeFrom.getItems().addAll("AM", "PM");
        timeTo.getItems().addAll("AM", "PM");

        if(!Objects.equals(paramStr, "")) {
            ResultSet res = get_avilability(paramStr, paramStr2);
            if (!res.next())
                messageLabel.setText("No rooms available for the search criteria");
            else
                display_availability(res);

        }
    }
    @FXML
    private void handleBack(ActionEvent ev) throws IOException {
        SceneController.changeScene(ev, "booking-view.fxml");
    }

    @FXML
    private void handleReset() {
        dateFrom.setValue(LocalDate.now());
        dateTo.setValue(null);
        timeFrom.getItems().addAll("AM", "PM");
        timeTo.getItems().addAll("AM", "PM");
        roomType.getItems().clear();
        roomType.setItems(RoomViewController.loadTypes());
        avlTable.getItems().clear();
        roomsData.clear();
        avlTable.setItems(roomsData);
    }

    @FXML
    private synchronized void checkAvailability() throws SQLException {
        LocalDate date_from = dateFrom.getValue();
        LocalDate date_to = dateTo.getValue();
        String time_from = (!timeFrom.getSelectionModel().isEmpty()) ? timeFrom.getSelectionModel().getSelectedItem().toLowerCase() : "";
        String time_to = (!timeTo.getSelectionModel().isEmpty()) ? timeTo.getSelectionModel().getSelectedItem().toLowerCase() : "";
        String room_type = (!roomType.getSelectionModel().isEmpty()) ? roomType.getSelectionModel().getSelectedItem().toLowerCase() : "";
        String param_str2 = "1";
        String param_str = "";
        param_str += (!room_type.equals("")) ? " AND r.type = '"+room_type+"' " : "";

        param_str2 += (date_from != null) ? " AND b.date_from = '"+date_from+"' " : "";
        param_str2 += (!time_from.equals("")) ? " AND b.time_from = '"+time_from+"' " : "";
        param_str2 += (date_to != null) ? " AND b.date_to = '"+date_to+"' " : "";
        param_str2 += (!time_to.equals("")) ? " AND b.time_to = '"+time_to+"' " : "";

        paramStr = param_str;
        paramStr2 = param_str2;
        ResultSet res = get_avilability(param_str, param_str2);
        if(!res.next())
            messageLabel.setText("No rooms available for the search criteria");
        else {
            display_availability(res);
        }
    }

    public static ResultSet get_avilability(String p_str1, String p_str2) {
        String q = "SELECT r.* FROM room r WHERE r.id NOT IN (SELECT b.room_id FROM booking b WHERE "+p_str2+") "+p_str1;
        System.err.println("query: "+q);

        return databaseController.executeSelectQuery(q);
    }

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
                Button action = new Button("Make a Booking");

                Rooms newRoom = new Rooms(a,roomName, roomCapacity, roomDetail, roomType, location, activeBookings, action);
                roomsData.add(newRoom);
                bookButton(action, selectedId);

                srNum.setCellValueFactory(new PropertyValueFactory<>("sr_num"));
                colRoomName.setCellValueFactory(new PropertyValueFactory<>("room_name"));
                colRoomCapacity.setCellValueFactory(new PropertyValueFactory<>("room_capacity"));
                colRoomDetail.setCellValueFactory(new PropertyValueFactory<>("room_detail"));
                colRoomType.setCellValueFactory(new PropertyValueFactory<>("room_type"));
                colRoomLocation.setCellValueFactory(new PropertyValueFactory<>("room_location"));
                colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

                avlTable.setItems(roomsData);

                a ++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void bookButton(Button action, int selectedId) {
        action.setOnAction(event -> {
            // Set the selected mode to "E" for edit mode
            selectedRoomId = selectedId;

            //System.err.println("selectedRoomId: "+selectedRoomId);

            // Change the scene to the room-edit.fxml
            try {
                SceneController.changeScene(event, "newbooking.fxml");
            } catch (IOException e) {
                // Throw a runtime exception if the scene change fails
                throw new RuntimeException(e);
            }
        });
    }
}
