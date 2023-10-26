package com.example.wetouriah;

public class ImageItem {
    String Id, image, pickup_request, image_status;

    public ImageItem(String id, String image, String pickup_request, String image_status) {
        Id = id;
        this.image = image;
        this.pickup_request = pickup_request;
        this.image_status = image_status;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPickup_request() {
        return pickup_request;
    }

    public void setPickup_request(String pickup_request) {
        this.pickup_request = pickup_request;
    }

    public String getImage_status() {
        return image_status;
    }

    public void setImage_status(String image_status) {
        this.image_status = image_status;
    }
}
