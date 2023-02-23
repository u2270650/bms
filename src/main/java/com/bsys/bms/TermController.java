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
import java.util.Date;

public class TermController {
    @FXML TableView<TermDates> tblTermDates;
    @FXML TableColumn<TermDates, Date> colStartDate;
    @FXML TableColumn<TermDates, Date> colEndDate;
    @FXML TableColumn<TermDates, String> colTitle;
    @FXML TableColumn<TermDates, String> colRmv;
    @FXML DatePicker fromDate;
    @FXML DatePicker toDate;
    @FXML TextField title;
    final DatabaseController databaseController = new DatabaseController();
    final ObservableList<TermDates> termdata = FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        showTermDates();
    }
    public void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.changeScene(actionEvent, "room-view.fxml");
    }

    public synchronized void deleteTermDate(Button remove, int termId) {
        remove.setOnAction(event -> {
            String q = "DELETE FROM term_dates WHERE id= "+termId;
            int isDeleted = databaseController.executeUpdateQuery(q);
            if(isDeleted > 0) {
                AlertController.showAlert("Success", "Term date has been deleted successfully!");
            }
            else {
                AlertController.showAlert( "Warning", "No record was deleted!");
            }

            showTermDates();
        });
    }

    public synchronized void addTermDate(ActionEvent actionEvent) throws IOException {
        LocalDate date_from = fromDate.getValue();
        LocalDate date_to = toDate.getValue();
        String term_title = title.getText();

        int err = 0;
        if(!FormValidator.validateDatePicker(fromDate, null)) err ++;
        if(!FormValidator.validateDatePicker(toDate, fromDate)) err ++;
        else if(!FormValidator.validateTextField(title)) err ++;

        if(err == 0) {
            // check if the startdate and enddate already exist in termdata
            boolean exists = false;
            for (TermDates term : termdata) {
                if (term.getStartdate().equals(date_from) && term.getEnddate().equals(date_to)) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                // the startdate and enddate already exist in termdata
                AlertController.showAlert("warning", "Same term dates exist, Please select different dates.");
            } else {
                String q = "INSERT INTO term_dates(title, datefrom, dateto) VALUES ('"+term_title+"', '"+date_from+"', '"+date_to+"')";
                int newId = databaseController.executeInsertQuery(q);
                Button btnRmv = new Button("Remove");
                deleteTermDate(btnRmv, newId);
                showTermDates();
            }
        }
    }

    private void showTermDates() {
        ObservableList<TermDates> data = getTermDates();
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startdate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("enddate"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("termTitle"));
        colRmv.setCellValueFactory(new PropertyValueFactory<>("btnRmv"));

        tblTermDates.setItems(data);
    }

    private ObservableList<TermDates> getTermDates() {
        String q = "SELECT * from term_dates";
        ResultSet resultSet = databaseController.executeSelectQuery(q);
        try{
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate startdate = resultSet.getDate("datefrom").toLocalDate();
                LocalDate enddate = resultSet.getDate("dateto").toLocalDate();
                String title = resultSet.getString("title");
                Button btnRmv = new Button("Remove");
                deleteTermDate(btnRmv, id);
                TermDates newRecord = new TermDates(id, startdate, enddate, title, btnRmv);
                termdata.add(newRecord);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return  termdata;
    }
}
