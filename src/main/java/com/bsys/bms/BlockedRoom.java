package com.bsys.bms;

import java.time.LocalDate;
import java.util.Date;

public class BlockedRoom {
    private Date datefrom;
    private Date dateto;
    private String reason;
    private int room_id;

    public BlockedRoom(int room_id, Date datefrom, Date dateto, String reason) {
        this.room_id = room_id;
        this.datefrom = datefrom;
        this.dateto = dateto;
        this.reason = reason;
    }

    public Date getDatefrom() {
        return datefrom;
    }

    public Date getDateto() {
        return dateto;
    }

    public String getReason() {
        return reason;
    }
}
