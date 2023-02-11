package com.bsys.bms;

import javafx.scene.control.Button;

import java.util.Date;

/**
 * Class representing a booking.
 */
public class Booking {
    private int id;
    private int roomId;
    private String organization;
    private String contactPerson;
    private String contactDetail;
    private Date dateFrom;
    private String timeFrom;
    private Date dateTo;
    private String timeTo;
    private String roomName;
    private String roomType;
    private int roomCapacity;
    private Button action;

    /**
     * Constructs a new instance of a booking.
     *
     * @param id            the id of the booking
     * @param roomId        the id of the room associated with the booking
     * @param organization  the name of the organization making the booking
     * @param contactPerson the name of the contact person for the booking
     * @param contactDetail the contact details for the contact person
     * @param dateFrom      the start date of the booking
     * @param timeFrom      the start time of the booking AM/PM
     * @param dateTo        the end date of the booking
     * @param timeTo        the end time of the booking AM/PM
     */
    public Booking(int id, int roomId, String organization, String contactPerson, String contactDetail, Date dateFrom, String timeFrom, Date dateTo, String timeTo, String roomName, String roomType, int roomCapacity, Button action) {
        this.id = id;
        this.roomId = roomId;
        this.organization = organization;
        this.contactPerson = contactPerson;
        this.contactDetail = contactDetail;
        this.dateFrom = dateFrom;
        this.timeFrom = timeFrom;
        this.dateTo = dateTo;
        this.timeTo = timeTo;
        this.roomName = roomName;
        this.roomType = roomType;
        this.roomCapacity = roomCapacity;
    }

    /**
     * Gets the id of the booking.
     *
     * @return the id of the booking
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the booking.
     *
     * @param id the id of the booking
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the room associated with the booking.
     *
     * @return the id of the room associated with the booking
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Sets the id of the room associated with the booking.
     *
     * @param roomId the id of the room associated with the booking
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Gets the name of the organization making the booking.
     *
     * @return the name of the organization making the booking
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets the name of the organization making the booking.
     *
     * @param organization the name of the organization making the booking
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * Gets the name of the contact person for the booking.
     *
     * @return the name of the contact person for the booking
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * Sets the name of the contact person for the booking.
     *
     * @param contactPerson the name of the contact person for the booking
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * Gets the contact details for the contact person.
     *
     * @return the contact details for the contact person
     */
    public String getContactDetail() {
        return contactDetail;
    }

    /**
     * Sets the contact details for the contact person.
     *
     * @param contactDetail the contact details for the contact person
     */
    public void setContactDetail(String contactDetail) {
        this.contactDetail = contactDetail;
    }

    /**
     * Gets the start date of the booking.
     *
     * @return the start date of the booking
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * Sets the start date of the booking.
     *
     * @param dateFrom the start date of the booking
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * Gets the start time of the booking.
     *
     * @return the start time of the booking
     */
    public String getTimeFrom() {
        return timeFrom;
    }

    /**
     * Sets the start time of the booking.
     *
     * @param timeFrom the start time of the booking
     */
    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    /**
     * Gets the end date of the booking.
     *
     * @return the end date of the booking
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * Sets the end date of the booking.
     *
     * @param dateTo the end date of the booking
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * Gets the end time of the booking.
     *
     * @return the end time of the booking
     */
    public String getTimeTo() {
        return timeTo;
    }

    /**
     * Sets the end time of the booking.
     *
     * @param timeTo the end time of the booking
     */
    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }
}