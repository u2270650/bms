package com.bsys.bms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class representing a term date range for a school semester.
 */
public class TermDates {

    private int id; // The unique identifier for the term date range.
    private LocalDate startdate; // The start date for the term.
    private LocalDate enddate; // The end date for the term.
    private String termTitle; // The title of the term.
    private Button btnRmv;
    final DatabaseController databaseController = new DatabaseController(); // The database controller.

    /**
     * Constructor for the TermDates class.
     *
     * @param id        The unique identifier for the term date range.
     * @param startdate The start date for the term.
     * @param enddate   The end date for the term.
     * @param termTitle The title of the term.
     */
    public TermDates(int id, LocalDate startdate, LocalDate enddate, String termTitle, Button btnRmv) {
        this.id = id;
        this.startdate = startdate;
        this.enddate = enddate;
        this.termTitle = termTitle;
        this.btnRmv = btnRmv;
    }

    /**
     * Gets the unique identifier for the term date range.
     *
     * @return The unique identifier for the term date range.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the term date range.
     *
     * @param id The unique identifier for the term date range.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the start date for the term.
     *
     * @return The start date for the term.
     */
    public LocalDate getStartdate() {
        return startdate;
    }

    /**
     * Sets the start date for the term.
     *
     * @param startdate The start date for the term.
     */
    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    /**
     * Gets the end date for the term.
     *
     * @return The end date for the term.
     */
    public LocalDate getEnddate() {
        return enddate;
    }

    /**
     * Sets the end date for the term.
     *
     * @param enddate The end date for the term.
     */
    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }

    /**
     * Gets the title of the term.
     *
     * @return The title of the term.
     */
    public String getTermTitle() {
        return termTitle;
    }

    /**
     * Sets the title of the term.
     *
     * @param termTitle The title of the term.
     */
    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    /**
     * Returns the button used for the action.
     *
     * @return the button used for the action
     */
    public Button getAction() {
        return btnRmv;
    }

    /**
     * Sets the button used for the action.
     *
     * @param action the button used for the action
     */
    public void setAction(Button action) {
        this.btnRmv = action;
    }

}