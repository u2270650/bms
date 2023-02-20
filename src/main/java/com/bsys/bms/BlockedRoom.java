package com.bsys.bms;

import java.time.LocalDate;
import java.util.Date;

public class BlockedRoom {
    private Date datefrom;
    private Date dateto;
    private String reason;
    private int room_id;

    // Constructor to create an instance of BlockedRoom
    public BlockedRoom(int room_id, Date datefrom, Date dateto, String reason) {
        this.room_id = room_id;
        this.datefrom = datefrom;
        this.dateto = dateto;
        this.reason = reason;
    }

    // Getter method to get the starting date of room block
    public Date getDatefrom() {
        return datefrom;
    }

    // Getter method to get the ending date of room block
    public Date getDateto() {
        return dateto;
    }

    // Getter method to get the reason for room block
    public String getReason() {
        return reason;
    }
}
