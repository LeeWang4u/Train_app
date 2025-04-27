package com.example.train_app.dto.request;


import java.io.Serializable;

public class CustomerDTO implements Serializable {
    private String cccd;

    private String email;

    private String fullName;

    private String phone;

    public String getCccd() {
        return cccd;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CustomerDTO() {
    }

    public CustomerDTO(String cccd, String phone, String fullName, String email) {
        this.cccd = cccd;
        this.phone = phone;
        this.fullName = fullName;
        this.email = email;
    }
}
