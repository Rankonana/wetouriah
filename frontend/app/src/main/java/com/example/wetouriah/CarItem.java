package com.example.wetouriah;

import java.io.Serializable;

public  class CarItem implements Serializable {
    private String id;
    private String car_owner;
    private String type;
    private String capacity;
    private String color;
    private String make;
    private String model;
    private String year;
    private String license_plate;



    private String is_approved;

    public CarItem(String id, String car_owner, String type, String capacity, String color, String make,
                   String model, String year, String license_plate,String is_approved) {
        this.id = id;
        this.car_owner = car_owner;
        this.type = type;
        this.capacity = capacity;
        this.color = color;
        this.make = make;
        this.model = model;
        this.year = year;
        this.license_plate = license_plate;
        this.is_approved = is_approved;

    }



    public String getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_owner() {
        return car_owner;
    }

    public void setCar_owner(String car_owner) {
        this.car_owner = car_owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }
}
