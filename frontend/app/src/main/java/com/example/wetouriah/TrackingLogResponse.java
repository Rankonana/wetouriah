package com.example.wetouriah;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackingLogResponse {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("pickup_request")
    @Expose
    private Integer pickupRequest;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("status_name")
    @Expose
    private String statusName;


    @SerializedName("estimated_arrival")
    @Expose
    private String estimatedArrival;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getPickupRequest() {
        return pickupRequest;
    }

    public void setPickupRequest(Integer pickupRequest) {
        this.pickupRequest = pickupRequest;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }


    public String getEstimatedArrival() {
        return estimatedArrival;
    }

    public void setEstimatedArrival(String estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }



}
