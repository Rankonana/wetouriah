package com.example.wetouriah;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PickupResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("start_datetime")
    @Expose
    private String startDatetime;
    @SerializedName("end_datetime")
    @Expose
    private String endDatetime;
    @SerializedName("duration")
    @Expose
    private Object duration;
    @SerializedName("tip")
    @Expose
    private Object tip;
    @SerializedName("rating")
    @Expose
    private Object rating;
    @SerializedName("last_known_location")
    @Expose
    private String lastKnownLocation;
    @SerializedName("is_delivered")
    @Expose
    private Boolean isDelivered;
    @SerializedName("request_pickup")
    @Expose
    private Integer requestPickup;
    @SerializedName("car")
    @Expose
    private Integer car;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Object getDuration() {
        return duration;
    }

    public void setDuration(Object duration) {
        this.duration = duration;
    }

    public Object getTip() {
        return tip;
    }

    public void setTip(Object tip) {
        this.tip = tip;
    }

    public Object getRating() {
        return rating;
    }

    public void setRating(Object rating) {
        this.rating = rating;
    }

    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(String lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public Boolean getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public Integer getRequestPickup() {
        return requestPickup;
    }

    public void setRequestPickup(Integer requestPickup) {
        this.requestPickup = requestPickup;
    }

    public Integer getCar() {
        return car;
    }

    public void setCar(Integer car) {
        this.car = car;
    }

}