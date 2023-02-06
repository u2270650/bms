package com.bsys.bms;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

public class Bookings {
    private int id;
    private int room_id;
    private String organisation;
    private String contact_person;
    private String contact_detail;
    private LocalDate date_from;
    private LocalTime time_from;
    private LocalDate date_to;
    private LocalTime time_to;
    private int user_id;
    private LocalDateTime datetime_booking;
    private Character is_paid;
    private Character status;
}
