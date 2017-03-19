package com.monordevelopers.tt.terratour.model;

public class UserModel {
    private String fullName;
    private String userName;
    private String password;
    private String phoneNumber;
    private String email;
    private String address;
    private String imgUrl;
    private int id;

    public UserModel(String fullName, String userName, String password, String phoneNumber,String email, String address, String imgUrl, int id) {
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.imgUrl = imgUrl;
        this.id = id;
        this.email = email;
    }
    public UserModel(String fullName, String userName, String password, String phoneNumber,String email, String address, String imgUrl) {
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.imgUrl = imgUrl;
        this.email = email;
    }
    public UserModel(String fullName, String userName, String password, String phoneNumber, String address, int id) {
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.id = id;
    }
    public UserModel(String fullName, String userName, String password, String phoneNumber, String address) {
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getId() {
        return id;
    }
}

