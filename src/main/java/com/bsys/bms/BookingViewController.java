package com.bsys.bms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

/**

 This class represents the controller for the BookingView screen in the application.

 It contains methods to handle user interactions and data loading for the booking table.
 */
public class BookingViewController {

    @FXML private TextField txtkeyword;
    @FXML private ComboBox<String> cmbtype;
    @FXML private DatePicker dtfrom;
    @FXML private DatePicker dtto;
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> sr_num;
    @FXML private TableColumn<Booking, Date> date_from;
    @FXML private TableColumn<Booking, Date> date_to;
    @FXML private TableColumn<Booking, String> room_name;
    @FXML private TableColumn<Booking, String> room_type;
    @FXML private TableColumn<Booking, String> room_capacity;
    @FXML private TableColumn<Booking, String> booking_details;
    @FXML private TableColumn<Booking, Button> action;
    final static DatabaseController databaseController = new DatabaseController();
    // Create a new empty ObservableList to store the retrieved data
    private static final ObservableList<Booking> bookingData = FXCollections.observableArrayList();
    public static int selectedBookingId = 0;

    /**

     Initializes the BookingView screen and sets up the table columns.

     It loads the available room types in the combo box and retrieves and displays the bookings.
     */
    @FXML
    public void initialize() {
        showBookings("1");
    }

    /**

     Handles the filtering of bookings based on the selected filters such as keyword, room type, and dates.

     It retrieves and displays the bookings matching the selected filters.
     */
    @FXML
    private void handleFilters() {
        String keyword = txtkeyword.getText();
        LocalDate dateFrom = dtfrom.getValue();
        LocalDate dateTo = dtto.getValue();
        String room_type = (!cmbtype.getSelectionModel().isEmpty()) ? cmbtype.getSelectionModel().getSelectedItem().toLowerCase() : "";
        String param_str = "1";
        param_str += (!room_type.isEmpty()) ? " AND r.type ='"+room_type+"'" : "";
        param_str += (dateFrom != null) ? " AND b.date_from >='"+dateFrom+"'" : "";
        param_str += (dateTo != null) ? " AND b.date_to <='"+dateTo+"'" : "";
        param_str += (!keyword.isEmpty()) ? " AND (r.name LIKE '%"+keyword+"%' OR b.organisation LIKE '%"+keyword+"%' OR b.contact_person LIKE '%"+keyword+"%' OR b.date_from LIKE '%"+keyword+"%' )" : "";

        showBookings(param_str);
    }

    /**

     Handles the reset of filters and displays all the bookings.
     It clears the filter fields and retrieves and displays all the bookings.
     */
    @FXML
    private void handleReset() {
        txtkeyword.setText("");
        dtfrom.setValue(null);
        dtto.setValue(null);
        cmbtype.setValue("");
        showBookings("1");
    }
    /**

     Handles the addition of a new booking.
     It changes the scene to the booking-edit.fxml screen.
     @param ev - ActionEvent representing the button click
     @throws IOException if the scene change fails
     */
    @FXML
    private void handleAddBooking(ActionEvent ev) throws IOException {
        SceneController.changeScene(ev, "booking-edit.fxml");
    }

    public void showBookings(String filter_param) {
        ObservableList<Booking> list = loadBookings(filter_param);

        cmbtype.setItems(RoomViewController.loadTypes());
        sr_num.setCellValueFactory(new PropertyValueFactory<>("id"));
        date_from.setCellValueFactory(new PropertyValueFactory<>("durationFrom"));
        date_to.setCellValueFactory(new PropertyValueFactory<>("durationTo"));
        room_name.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        room_type.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        room_capacity.setCellValueFactory(new PropertyValueFactory<>("roomCapacity"));
        booking_details.setCellValueFactory(new PropertyValueFactory<>("bookingDetail"));
        //action.setCellValueFactory(new PropertyValueFactory<>("action"));

        bookingTable.setItems(list);
    }

    /**

     This method retrieves a list of bookings from the database based on the provided filter parameter and returns an ObservableList of Booking objects.

     @param filter_param a string representing the filter parameter used to retrieve bookings from the database

     @return an ObservableList of Booking objects containing the retrieved data from the database
     */
    public static ObservableList<Booking> loadBookings(String filter_param) {
        // Construct the SQL query string to retrieve the required booking data from the database
        String q = "SELECT r.name, r.type, r.capacity, b.* FROM booking b join room r on b.room_id = r.id WHERE "+ filter_param;

        // Execute the SQL query and store the result in a ResultSet object
        ResultSet resultSet = databaseController.executeSelectQuery(q);

        try {
            // Clear any existing data in the ObservableList
            bookingData.clear();
            // Loop through each row of the ResultSet and extract the relevant data
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String organization = resultSet.getString("organisation");
                String contactPerson = resultSet.getString("contact_person");
                String contactDetail = resultSet.getString("contact_detail");
                Date dateFrom = resultSet.getDate("date_from");
                String timeFrom = resultSet.getString("time_from");
                Date dateTo = resultSet.getDate("date_to");
                String timeTo = resultSet.getString("time_to");
                String roomName = resultSet.getString("name");
                String roomType = resultSet.getString("type");
                int roomCapacity = resultSet.getInt("capacity");
                Button action = new Button("Edit");

                // Construct a booking detail string by concatenating the relevant fields
                String booking_detail = "";
                booking_detail += (contactPerson != null && contactPerson != "") ? contactPerson+"\n" : "";
                booking_detail += (organization != null && organization != "") ? organization+"\n" : "";
                booking_detail += (contactDetail != null && contactDetail != "") ? contactDetail+"\n" : "";
                String duration_from = dateFrom + " | " +timeFrom;
                String duration_to = dateTo + " | " +timeTo;

                // Call the editButton() method to set up the action button for the new record
                editButton(action, id);

                // Create a new Booking object and add it to the ObservableList with the retrieved data
                Booking newRecord = new Booking(id, booking_detail, duration_from, duration_to, roomName, roomType, roomCapacity, action);
                bookingData.add(newRecord);
            }

        }
        catch (SQLException e) {
            // Print the stack trace of any SQLException that occurs during execution of the SQL query
            e.printStackTrace();
        }

        // Return the ObservableList of Booking objects containing the retrieved data
        return bookingData;
    }

    /**

     Sets an on-action event handler for the given Button.

     When the Button is clicked, it sets the selectedBookingId to the given selectedId,

     changes the scene to "booking-edit.fxml", and throws a RuntimeException if the scene change fails.

     @param action the Button to set the on-action event handler for

     @param selectedId the ID of the booking to be edited
     */
    private static void editButton(Button action, int selectedId) {
        action.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Set the selected id
                selectedBookingId = selectedId;

                //System.err.println("selectedRoomId: "+selectedRoomId);

                // Change the scene to the room-edit.fxml
                try {
                    SceneController.changeScene(event, "booking-edit.fxml");
                } catch (IOException e) {
                    // Throw a runtime exception if the scene change fails
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
