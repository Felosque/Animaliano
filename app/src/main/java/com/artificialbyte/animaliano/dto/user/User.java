package com.artificialbyte.animaliano.dto.user;

import java.io.Serializable;

public class User implements Serializable {

    private String uid;
    private String name;
    private String lastName;
    private String userName = "none";
    private String email;
    private String description;
    private String birthDate;
    private String profilePhoto;
    private String city_municipality;
    private String department;
    private String nit;
    private boolean verify;
    private String rol;
    private String phone;
    private double balance;

    public User() {
        this.uid = "";
        this.name = "";
        this.lastName = "";
        this.userName = "";
        this.email = "";
        this.description = "";
        this.birthDate = "";
        this.profilePhoto = "";
        this.nit = "";
        this.verify = false;
        this.rol = null;
        this.city_municipality = "";
        this.department = "";
        this.phone = null;
        this.balance = 0.0;
    }

    public User(String description, String rol, String uid, String name, String lastName, String userName, String email, Long birthDate, String profilePhoto, String nit, boolean verify, double balance) {
        this.uid = uid;
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.description = description;
        this.birthDate = "";
        this.profilePhoto = profilePhoto;
        this.nit = nit;
        this.verify = verify;
        this.rol = rol;
        this.balance = balance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity_municipality() {
        return city_municipality;
    }

    public void setCity_municipality(String city_municipality) {
        this.city_municipality = city_municipality;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", city_municipality='" + city_municipality + '\'' +
                ", department='" + department + '\'' +
                ", nit='" + nit + '\'' +
                ", verify=" + verify +
                ", rol='" + rol + '\'' +
                ", phone='" + phone + '\'' +
                ", balance=" + balance +
                '}';
    }
}
