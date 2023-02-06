package com.bsys.bms;

import javafx.event.ActionEvent;
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

public class RoomController {
    @FXML private ComboBox<String> cmbtype;
    private DatabaseController databaseController = new DatabaseController();
    @FXML
    private void initialize() {
        cmbtype.setItems(loadTypes());
    }
    public void handleFilters(ActionEvent actionEvent) {
    }

    public void handleReset(ActionEvent actionEvent) {
    }

    private ObservableList<Rooms> loadRooms(String filter_param) {

        ObservableList<Rooms> data = FXCollections.observableArrayList();
        ResultSet resultSet = databaseController.executeSelectQuery("SELECT r FROM room WHERE "+ filter_param);
        try {
            while (resultSet.next()) {
//                data.addAll()
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
}
