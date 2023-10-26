package com.example.wetouriah;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagesResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("pickup_request")
    @Expose
    private Integer pickupRequest;
    @SerializedName("image_status")
    @Expose
    private Integer imageStatus;

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

    public Integer getPickupRequest() {
        return pickupRequest;
    }

    public void setPickupRequest(Integer pickupRequest) {
        this.pickupRequest = pickupRequest;
    }

    public Integer getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(Integer imageStatus) {
        this.imageStatus = imageStatus;
    }
}
