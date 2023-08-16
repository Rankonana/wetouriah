package com.example.wetouriah;

import java.io.Serializable;

public class DriversLicense implements Serializable {
    String id,license_owner, fullname  , identity_number , date_of_birth , license_number , expiry_date , country_of_issue , code , restrictions , gender ,
    date_of_issue,
    uploadLicense ,
    is_approved ;

    public DriversLicense(String id, String license_owner, String fullname,
                          String identity_number, String date_of_birth, String license_number,
                          String expiry_date, String country_of_issue, String code, String restrictions, String gender,
                          String date_of_issue, String uploadLicense, String is_approved) {
        this.id = id;
        this.license_owner = license_owner;
        this.fullname = fullname;
        this.identity_number = identity_number;
        this.date_of_birth = date_of_birth;
        this.license_number = license_number;
        this.expiry_date = expiry_date;
        this.country_of_issue = country_of_issue;
        this.code = code;
        this.restrictions = restrictions;
        this.gender = gender;
        this.date_of_issue = date_of_issue;
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

    public String getIdentity_number() {
        return identity_number;
    }

    public void setIdentity_number(String identity_number) {
        this.identity_number = identity_number;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
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

    public String getCountry_of_issue() {
        return country_of_issue;
    }

    public void setCountry_of_issue(String country_of_issue) {
        this.country_of_issue = country_of_issue;
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


    public String getDate_of_issue() {
        return date_of_issue;
    }

    public void setDate_of_issue(String date_of_issue) {
        this.date_of_issue = date_of_issue;
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
