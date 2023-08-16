package com.example.wetouriah;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WarehouseResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("cctv")
    @Expose
    private Boolean cctv;
    @SerializedName("armed_response")
    @Expose
    private Boolean armedResponse;
    @SerializedName("fire_safety_and_management")
    @Expose
    private Boolean fireSafetyAndManagement;
    @SerializedName("parking_space")
    @Expose
    private Boolean parkingSpace;
    @SerializedName("operating_hours")
    @Expose
    private String operatingHours;
    @SerializedName("is_approved")
    @Expose
    private Boolean isApproved;
    @SerializedName("warehouse_owner")
    @Expose
    private Integer warehouseOwner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Boolean getCctv() {
        return cctv;
    }

    public void setCctv(Boolean cctv) {
        this.cctv = cctv;
    }

    public Boolean getArmedResponse() {
        return armedResponse;
    }

    public void setArmedResponse(Boolean armedResponse) {
        this.armedResponse = armedResponse;
    }

    public Boolean getFireSafetyAndManagement() {
        return fireSafetyAndManagement;
    }

    public void setFireSafetyAndManagement(Boolean fireSafetyAndManagement) {
        this.fireSafetyAndManagement = fireSafetyAndManagement;
    }

    public Boolean getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(Boolean parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getWarehouseOwner() {
        return warehouseOwner;
    }

    public void setWarehouseOwner(Integer warehouseOwner) {
        this.warehouseOwner = warehouseOwner;
    }

}
