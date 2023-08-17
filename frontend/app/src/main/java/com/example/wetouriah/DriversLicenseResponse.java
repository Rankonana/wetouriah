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
    @SerializedName("identity_number")
    @Expose
    private String identityNumber;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("license_number")
    @Expose
    private Object licenseNumber;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("country_of_issue")
    @Expose
    private String countryOfIssue;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("restrictions")
    @Expose
    private String restrictions;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("date_of_issue")
    @Expose
    private String dateOfIssue;
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

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getCountryOfIssue() {
        return countryOfIssue;
    }

    public void setCountryOfIssue(String countryOfIssue) {
        this.countryOfIssue = countryOfIssue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
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
