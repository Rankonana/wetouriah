package com.example.wetouriah;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class PickUpRequestItem implements Serializable {

    String id;
    String customer ;
    String tracking_number ;
    String request_time ;
    String recipient_name ;
    String recipient_phone;
    String pickup_location ;
    String dropoff_location;
    String volume ;
    String weight ;
    String parcel_description;
    String price_to_pay ;
    String status ;
    String car  ;
    String duration ;
    String rating ;
    String start_datetime ;
    String end_datetime ;




    public PickUpRequestItem(String id, String customer, String tracking_number, String request_time, String recipient_name, String recipient_phone, String pickup_location, String dropoff_location, String volume, String weight, String parcel_description, String price_to_pay, String status, String car, String duration, String rating, String start_datetime, String end_datetime) {
        this.id = id;
        this.customer = customer;
        this.tracking_number = tracking_number;
        this.request_time = request_time;
        this.recipient_name = recipient_name;
        this.recipient_phone = recipient_phone;
        this.pickup_location = pickup_location;
        this.dropoff_location = dropoff_location;
        this.volume = volume;
        this.weight = weight;
        this.parcel_description = parcel_description;
        this.price_to_pay = price_to_pay;
        this.status = status;
        this.car = car;
        this.duration = duration;
        this.rating = rating;
        this.start_datetime = start_datetime;
        this.end_datetime = end_datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTracking_number() {
        return tracking_number;
    }

    public void setTracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
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

    public String getParcel_description() {
        return parcel_description;
    }

    public void setParcel_description(String parcel_description) {
        this.parcel_description = parcel_description;
    }

    public String getPrice_to_pay() {
        return price_to_pay;
    }

    public void setPrice_to_pay(String price_to_pay) {
        this.price_to_pay = price_to_pay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(String start_datetime) {
        this.start_datetime = start_datetime;
    }

    public String getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(String end_datetime) {
        this.end_datetime = end_datetime;
    }


    public static String toJson(List<PickUpRequestItem> pickUpRequestItemItems) {
        Gson gson = new Gson();
        return gson.toJson(pickUpRequestItemItems);
    }

    public static List<PickUpRequestItem> fromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<PickUpRequestItem>>() {}.getType();
        return gson.fromJson(json, type);
    }


}
