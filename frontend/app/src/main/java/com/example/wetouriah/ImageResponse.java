package com.example.wetouriah;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageResponse {

    @SerializedName("request_pickup_images")
    @Expose
    private String requestPickupImages;

    public String getRequestPickupImages() {
        return requestPickupImages;
    }

    public void setRequestPickupImages(String requestPickupImages) {
        this.requestPickupImages = requestPickupImages;
    }

}
