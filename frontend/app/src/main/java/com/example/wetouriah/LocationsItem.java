package com.example.wetouriah;

import java.io.Serializable;

public class LocationsItem implements Serializable {

    String location;
    String timestamp;
    String status ;


    public LocationsItem(String location, String timestamp,String status) {
        this.location = location;
        this.timestamp = timestamp;
        this.status = status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
