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
    @FXML private TableColumn action;
    private static DatabaseController databaseController = new DatabaseController();
    public static int selectedBookingId = 0;
    @FXML
    public void initialize() {
        cmbtype.setItems(RoomViewController.loadTypes());
        sr_num.setCellValueFactory(new PropertyValueFactory<>("id"));
        date_from.setCellValueFactory(new PropertyValueFactory<>("durationFrom"));
        date_to.setCellValueFactory(new PropertyValueFactory<>("durationTo"));
        room_name.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        room_type.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        room_capacity.setCellValueFactory(new PropertyValueFactory<>("roomCapacity"));
        booking_details.setCellValueFactory(new PropertyValueFactory<>("bookingDetail"));
        //action.setCellValueFactory(new PropertyValueFactory<>("action"));

        bookingTable.setItems(loadBookings("1"));
    }

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

        bookingTable.setItems(loadBookings(param_str));
    }

    @FXML
    private void handleReset() {
        txtkeyword.setText("");
        dtfrom.setValue(null);
        dtto.setValue(null);
        cmbtype.setValue("");
        bookingTable.setItems(loadBookings("1"));
    }

    @FXML
    private void handleAddBooking(ActionEvent ev) throws IOException {
        SceneController.changeScene(ev, "booking-edit.fxml");
    }

    private ObservableList<Booking> loadBookings(String filter_param) {
        ObservableList<Booking> bookingData = FXCollections.observableArrayList();

        String q = "SELECT r.name, r.type, r.capacity, b.* FROM booking b join room r on b.room_id = r.id WHERE "+ filter_param;
        ResultSet resultSet = databaseController.executeSelectQuery(q);

        try {
            bookingData.clear();
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

                String booking_detail = "";
                booking_detail += (contactPerson != null && contactPerson != "") ? contactPerson+"\n" : "";
                booking_detail += (organization != null && organization != "") ? organization+"\n" : "";
                booking_detail += (contactDetail != null && contactDetail != "") ? contactDetail+"\n" : "";
                String duration_from = dateFrom + " | " +timeFrom;
                String duration_to = dateTo + " | " +timeTo;

                editButton(action, id);

                // Add a new room object to the `data` list with the retrieved data
                Booking newRecord = new Booking(id, booking_detail, duration_from, duration_to, roomName, roomType, roomCapacity, action);
                bookingData.add(newRecord);
            }
        }
        catch (SQLException e) {
            // Print the stack trace of the exception if there is an error in the database query
            e.printStackTrace();
        }


        return bookingData;
    }

    private void editButton(Button action, int selectedId) {
        action.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Set the selected mode to "E" for edit mode
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
