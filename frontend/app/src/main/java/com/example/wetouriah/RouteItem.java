package com.example.wetouriah;

public class RouteItem {
    String tracking_number, status, location;

    public RouteItem(String tracking_number, String status, String location) {
        this.tracking_number = tracking_number;
        this.status = status;
        this.location = location;
    }

    public String getTracking_number() {
        return tracking_number;
    }

    public void setTracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
