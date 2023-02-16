package com.bsys.bms;

import javafx.scene.control.Button;

import java.util.Date;

/**

 Class representing a booking.
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

    // Getter methods
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

    // Setter methods
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