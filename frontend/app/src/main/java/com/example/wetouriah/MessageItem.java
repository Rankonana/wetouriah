package com.example.wetouriah;

import java.io.Serializable;

public class MessageItem implements Serializable {




    String id;
    String message;
    String  timestamp;
    String pickup;
    String sender;

    public MessageItem(String id, String message, String timestamp, String pickup, String sender) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.pickup = pickup;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
