package com.bsys.bms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class NewBookingController {
    @FXML DatePicker fromDate;
    @FXML DatePicker toDate;
    @FXML Label roomId;
    @FXML ComboBox<String> fromTime;
    @FXML ComboBox<String> toTime;
    @FXML TextField orgName;
    @FXML TextField contactPerson;
    @FXML TextField contactDetail;
    final int selectedRoomId = BookingEditController.selectedRoomId;
    final static DatabaseController databaseController = new DatabaseController();

    @FXML
    private void initialize() {
        roomId.setText("You are making a booking for Room #"+selectedRoomId);
        fromDate.setValue(LocalDate.now());
        fromTime.getItems().addAll("AM", "PM");
        toTime.getItems().addAll("AM", "PM");
    }
    @FXML
    private synchronized void handleBoooking(ActionEvent ev) throws IOException {
        LocalDate date_from = fromDate.getValue();
        LocalDate date_to = toDate.getValue();
        String time_from = (!fromTime.getSelectionModel().isEmpty()) ? fromTime.getSelectionModel().getSelectedItem().toLowerCase() : "";
        String time_to = (!toTime.getSelectionModel().isEmpty()) ? toTime.getSelectionModel().getSelectedItem().toLowerCase() : "";
        String org_name = orgName.getText();
        String cont_person = contactPerson.getText();
        String cont_detail = contactDetail.getText();

        int err = 0;
        String param_str = "AND r.id = '"+selectedRoomId+"'";
        String param_str2 = "1";
        param_str2 += (date_from != null) ? " AND b.date_from = '"+date_from+"' " : "";
        param_str2 += (!time_from.equals("")) ? " AND b.time_from = '"+time_from+"' " : "";
        param_str2 += (date_to != null) ? " AND b.date_to = '"+date_to+"' " : "";
        param_str2 += (!time_to.equals("")) ? " AND b.time_to = '"+time_to+"' " : "";

        if(!FormValidator.validateDatePicker(fromDate, null)) err ++;
        else if(!FormValidator.validateComboBox(fromTime)) err ++;
        else if(!FormValidator.validateDatePicker(toDate, fromDate)) err ++;
        else if(!FormValidator.validateComboBox(toTime)) err ++;
        else if(!FormValidator.validateTextField(orgName)) err ++;
        else if(!FormValidator.validateTextField(contactPerson)) err ++;
        else if(!FormValidator.validateTextField(contactDetail)) err ++;

        if(err == 0) {
            ResultSet res = BookingEditController.get_availability(param_str, param_str2);

            try{
                if(!res.next()) {
                    // no available booking;
                    AlertController.showAlert("warning", "There are no rooms available!");
                }
                else {
                    // booking available cannot proceed
                    String q = "INSERT INTO booking(room_id, organisation, contact_person, contact_detail, date_from, time_from, date_to, time_to) VALUES ('"+selectedRoomId+"', '"+org_name+"', '"+cont_person+"', '"+cont_detail+"', '"+date_from+"', '"+time_from+"', '"+date_to+"', '"+time_to+"')";
                    int result = databaseController.executeInsertQuery(q);
                    if(result > 0) {
                        AlertController.showAlert("success", "Booking created successfully");
                        SceneController.changeScene(ev, "booking-view.fxml");
                        BookingViewController.loadBookings("1");
                    }
                    else {
                        AlertController.showAlert("error", "Something went wrong in creating the booking. Try again later!");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent ev) throws IOException {
        SceneController.changeScene(ev, "booking-edit.fxml");
    }

}
