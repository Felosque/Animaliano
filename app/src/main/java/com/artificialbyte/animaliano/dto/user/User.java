package com.artificialbyte.animaliano.dto.user;

public class User {

    private String uid;
    private String name;
    private String lastName;
    private String userName = "none";
    private String email;
    private String description;
    private Long birthDate;
    private String profilePhoto;
    private String nit;
    private boolean verify;
    private String userType;

    public User() {
        this.uid = "none";
        this.name = "none";
        this.lastName = "none";
        this.userName = "none";
        this.email = "none";
        this.description = "none";
        this.birthDate = Long.parseLong("102313245");
        this.profilePhoto = "none";
        this.nit = "none";
        this.verify = true;
        this.userType = "none";
    }

    public User(String description, String userType, String uid, String name, String lastName, String userName, String email, Long birthDate, String profilePhoto, String nit, boolean verify) {
        this.uid = uid;
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.description = description;
        this.birthDate = birthDate;
        this.profilePhoto = profilePhoto;
        this.nit = nit;
        this.verify = verify;
        this.userType = userType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
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


}
