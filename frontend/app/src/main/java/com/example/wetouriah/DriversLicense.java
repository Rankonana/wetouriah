package com.example.wetouriah;

import android.content.Intent;
import android.view.MenuItem;

import java.io.Serializable;

public class DriversLicense implements Serializable {
    String id,license_owner,fullname,  license_number, expiry_date, uploadLicense,is_approved;

    public DriversLicense(String id, String license_owner, String fullname, String license_number, String expiry_date, String uploadLicense, String is_approved) {
        this.id = id;
        this.license_owner = license_owner;
        this.fullname = fullname;
        this.license_number = license_number;
        this.expiry_date = expiry_date;
        this.uploadLicense = uploadLicense;
        this.is_approved = is_approved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicense_owner() {
        return license_owner;
    }

    public void setLicense_owner(String license_owner) {
        this.license_owner = license_owner;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getUploadLicense() {
        return uploadLicense;
    }

    public void setUploadLicense(String uploadLicense) {
        this.uploadLicense = uploadLicense;
    }

    public String getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }
}
