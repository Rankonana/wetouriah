package com.example.wetouriah;

public class WareHouseItem {

    String id;

    String address;

    public WareHouseItem(String id, String address)
    {
        this.id = id;
        this.address = address;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
