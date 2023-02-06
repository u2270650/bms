package com.bsys.bms;

public class Rooms {
    private int id;
    private String name;
    private String detail;
    private String type;
    private int capacity;
    private String location;

    public Rooms(int id, String name, String detail, String type, int capacity, String location) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.type = type;
        this.capacity = capacity;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) { this.location = location; }
}