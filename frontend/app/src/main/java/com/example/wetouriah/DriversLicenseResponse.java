package com.example.wetouriah;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriversLicenseResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fullname")
    @Expose
    private String fullname;


    @SerializedName("license_number")
    @Expose
    private Object licenseNumber;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;

    @SerializedName("uploadLicense")
    @Expose
    private String uploadLicense;
    @SerializedName("is_approved")
    @Expose
    private Boolean isApproved;
    @SerializedName("license_owner")
    @Expose
    private Integer licenseOwner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public Object getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(Object licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }


    public String getUploadLicense() {
        return uploadLicense;
    }

    public void setUploadLicense(String uploadLicense) {
        this.uploadLicense = uploadLicense;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getLicenseOwner() {
        return licenseOwner;
    }

    public void setLicenseOwner(Integer licenseOwner) {
        this.licenseOwner = licenseOwner;
    }

}
