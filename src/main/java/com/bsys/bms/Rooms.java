package com.bsys.bms;

import javafx.scene.control.Button;

public class Rooms {
    private Integer sr_num;
    private String room_name;
    private Integer room_capacity;
    private String room_detail;
    private String room_type;
    private Integer active_bookings;
    private Button action;

    public Rooms(Integer sr_num, String room_name, Integer room_capacity, String room_detail, String room_type, Integer active_bookings, Button action) {
        this.sr_num = sr_num;
        this.room_name = room_name;
        this.room_capacity = room_capacity;
        this.room_detail = room_detail;
        this.room_type = room_type;
        this.active_bookings = active_bookings;
        this.action = action;
    }

    public Integer getSr_num() {
        return sr_num;
    }

    public void setSr_num(Integer sr_num) {
        this.sr_num = sr_num;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public Integer getRoom_capacity() {
        return room_capacity;
    }

    public void setRoom_capacity(Integer room_capacity) {
        this.room_capacity = room_capacity;
    }

    public String getRoom_detail() {
        return room_detail;
    }

    public void setRoom_detail(String room_detail) {
        this.room_detail = room_detail;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public Integer getActive_bookings() {
        return active_bookings;
    }

    public void setActive_bookings(Integer active_bookings) {
        this.active_bookings = active_bookings;
    }

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }


}
