package com.example.wetouriah;

import java.util.List;
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
    @SerializedName("date_and_time_pickup")
    @Expose
    private String dateAndTimePickup;
    @SerializedName("recipient_name")
    @Expose
    private String recipientName;
    @SerializedName("recipient_phone")
    @Expose
    private String recipientPhone;
    @SerializedName("pickup_location")
    @Expose
    private Object pickupLocation;
    @SerializedName("dropoff_location")
    @Expose
    private Object dropoffLocation;
    @SerializedName("volume")
    @Expose
    private Object volume;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("price_to_pay")
    @Expose
    private Object priceToPay;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("customer")
    @Expose
    private Integer customer;
    @SerializedName("images")
    @Expose
    private List<Integer> images;

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

    public String getDateAndTimePickup() {
        return dateAndTimePickup;
    }

    public void setDateAndTimePickup(String dateAndTimePickup) {
        this.dateAndTimePickup = dateAndTimePickup;
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

    public Object getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(Object pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Object getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(Object dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public Object getVolume() {
        return volume;
    }

    public void setVolume(Object volume) {
        this.volume = volume;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Object getPriceToPay() {
        return priceToPay;
    }

    public void setPriceToPay(Object priceToPay) {
        this.priceToPay = priceToPay;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public List<Integer> getImages() {
        return images;
    }

    public void setImages(List<Integer> images) {
        this.images = images;
    }
}
