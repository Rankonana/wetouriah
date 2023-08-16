package com.example.wetouriah;

import java.io.Serializable;

public class UserItem implements Serializable {

    String id,username,address,first_name,last_name,email,is_active,profile_picture,role,phone_number;

    public UserItem(String id, String username, String address, String first_name, String last_name, String email, String is_active, String profile_picture, String role, String phone_number) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.is_active = is_active;
        this.profile_picture = profile_picture;
        this.role = role;
        this.phone_number = phone_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}




