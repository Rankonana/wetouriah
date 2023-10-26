package com.example.wetouriah;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestPickupResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tracking_number")
    @Expose
    private String trackingNumber;
    @SerializedName("request_time")
    @Expose
    private String requestTime;
    @SerializedName("recipient_name")
    @Expose
    private String recipientName;
    @SerializedName("recipient_phone")
    @Expose
    private String recipientPhone;
    @SerializedName("pickup_location")
    @Expose
    private String pickupLocation;
    @SerializedName("dropoff_location")
    @Expose
    private String dropoffLocation;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("parcel_description")
    @Expose
    private String parcelDescription;
    @SerializedName("price_to_pay")
    @Expose
    private String priceToPay;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("start_datetime")
    @Expose
    private String startDatetime;
    @SerializedName("end_datetime")
    @Expose
    private String endDatetime;
    @SerializedName("customer")
    @Expose
    private Integer customer;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("car")
    @Expose
    private Integer car;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
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

    public String getParcelDescription() {
        return parcelDescription;
    }

    public void setParcelDescription(String parcelDescription) {
        this.parcelDescription = parcelDescription;
    }

    public String getPriceToPay() {
        return priceToPay;
    }

    public void setPriceToPay(String priceToPay) {
        this.priceToPay = priceToPay;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCar() {
        return car;
    }

    public void setCar(Integer car) {
        this.car = car;
    }

}