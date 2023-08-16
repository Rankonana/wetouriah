package com.example.wetouriah;

import java.io.Serializable;

public class WareHouseItemAdmin implements Serializable {

String id, image, address, volume;
String cctv , armed_response, fire_safety_and_management, parking_space, is_approved,operating_hours, warehouse_owner;


    public WareHouseItemAdmin(String id, String image, String address, String volume, String cctv, String armed_response, String fire_safety_and_management, String parking_space, String is_approved, String operating_hours, String warehouse_owner) {
        this.id = id;
        this.image = image;
        this.address = address;
        this.volume = volume;
        this.cctv = cctv;
        this.armed_response = armed_response;
        this.fire_safety_and_management = fire_safety_and_management;
        this.parking_space = parking_space;
        this.is_approved = is_approved;
        this.operating_hours = operating_hours;
        this.warehouse_owner = warehouse_owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCctv() {
        return cctv;
    }

    public void setCctv(String cctv) {
        this.cctv = cctv;
    }

    public String getArmed_response() {
        return armed_response;
    }

    public void setArmed_response(String armed_response) {
        this.armed_response = armed_response;
    }

    public String getFire_safety_and_management() {
        return fire_safety_and_management;
    }

    public void setFire_safety_and_management(String fire_safety_and_management) {
        this.fire_safety_and_management = fire_safety_and_management;
    }

    public String getParking_space() {
        return parking_space;
    }

    public void setParking_space(String parking_space) {
        this.parking_space = parking_space;
    }

    public String getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }

    public String getOperating_hours() {
        return operating_hours;
    }

    public void setOperating_hours(String operating_hours) {
        this.operating_hours = operating_hours;
    }

    public String getWarehouse_owner() {
        return warehouse_owner;
    }

    public void setWarehouse_owner(String warehouse_owner) {
        this.warehouse_owner = warehouse_owner;
    }
}
