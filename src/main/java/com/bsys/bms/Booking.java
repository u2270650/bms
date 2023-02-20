package com.bsys.bms;

import javafx.scene.control.Button;

import java.util.Date;

/**

 Booking class represents a booking object with details such as id, booking details,

 duration from and to, room name, room type, room capacity and action button.
 */
public class Booking {
    private int id;
    private String bookingDetail;
    private String durationFrom;
    private String durationTo;
    private String roomName;
    private String roomType;
    private int roomCapacity;
    private Button action;

    /**

     Constructor that creates a new booking object with the specified details.
     @param id the unique identifier of the booking.
     @param bookingDetail the details of the booking.
     @param durationFrom the starting time of the booking.
     @param durationTo the ending time of the booking.
     @param roomName the name of the room that is booked.
     @param roomType the type of the room that is booked.
     @param roomCapacity the capacity of the room that is booked.
     @param action the action button associated with the booking.
     */
    public Booking(int id, String bookingDetail, String durationFrom, String durationTo, String roomName, String roomType, int roomCapacity, Button action) {
        this.id = id;
        this.bookingDetail = bookingDetail;
        this.durationFrom = durationFrom;
        this.durationTo = durationTo;
        this.roomName = roomName;
        this.roomType = roomType;
        this.roomCapacity = roomCapacity;
        this.action = action;
    }
    // Getter methods to get the values of the instance variables
    public int getId() {
        return id;
    }

    public String getBookingDetail() {
        return bookingDetail;
    }

    public String getDurationFrom() {
        return durationFrom;
    }

    public String getDurationTo() {
        return durationTo;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public Button getAction() {
        return action;
    }

    // Setter methods to set the values of the instance variables
    public void setId(int id) {
        this.id = id;
    }

    public void setBookingDetail(String bookingDetail) {
        this.bookingDetail = bookingDetail;
    }

    public void setDurationFrom(String durationFrom) {
        this.durationFrom = durationFrom;
    }

    public void setDurationTo(String durationTo) {
        this.durationTo = durationTo;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public void setAction(Button action) {
        this.action = action;
    }
}