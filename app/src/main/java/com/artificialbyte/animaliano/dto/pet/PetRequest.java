package com.artificialbyte.animaliano.dto.pet;

import java.io.Serializable;

public class PetRequest  implements Serializable {

    private String uid;
    private String uidUser;
    private String fullName;
    private String contactPhone;
    private String description;

    public PetRequest(String uid, String uidUser, String fullName, String contactPhone, String description) {
        this.uid = uid;
        this.uidUser = uidUser;
        this.fullName = fullName;
        this.contactPhone = contactPhone;
        this.description = description;
    }

    public PetRequest() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
