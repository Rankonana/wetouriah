package com.example.wetouriah;

import java.io.Serializable;

public class PickUpRequestItem implements Serializable {




    String id;
    String tracking_number;
    String customer;
    String  request_time;
    String  date_and_time_pickup;
    String  recipient_name;
    String  recipient_phone;
    String  pickup_location;
    String  dropoff_location;
    String  volume;
    String  weight;
    String  price_to_pay;
    String images;
    String status;

    public PickUpRequestItem(String id, String tracking_number, String customer, String request_time, String date_and_time_pickup, String recipient_name, String recipient_phone, String pickup_location, String dropoff_location, String volume, String weight, String price_to_pay, String images, String status) {
        this.id = id;
        this.tracking_number = tracking_number;
        this.customer = customer;
        this.request_time = request_time;
        this.date_and_time_pickup = date_and_time_pickup;
        this.recipient_name = recipient_name;
        this.recipient_phone = recipient_phone;
        this.pickup_location = pickup_location;
        this.dropoff_location = dropoff_location;
        this.volume = volume;
        this.weight = weight;
        this.price_to_pay = price_to_pay;
        this.images = images;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTracking_number() {
        return tracking_number;
    }

    public void setTracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getDate_and_time_pickup() {
        return date_and_time_pickup;
    }

    public void setDate_and_time_pickup(String date_and_time_pickup) {
        this.date_and_time_pickup = date_and_time_pickup;
    }

    public String getRecipient_name() {
        return recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getRecipient_phone() {
        return recipient_phone;
    }

    public void setRecipient_phone(String recipient_phone) {
        this.recipient_phone = recipient_phone;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getDropoff_location() {
        return dropoff_location;
    }

    public void setDropoff_location(String dropoff_location) {
        this.dropoff_location = dropoff_location;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }



    public String getPrice_to_pay() {
        return price_to_pay;
    }

    public void setPrice_to_pay(String price_to_pay) {
        this.price_to_pay = price_to_pay;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
