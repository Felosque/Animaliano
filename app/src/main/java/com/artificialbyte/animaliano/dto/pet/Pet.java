package com.artificialbyte.animaliano.dto.pet;

import java.util.ArrayList;
import java.util.Map;

public class Pet {

    private String uid;
    private String uidFoundation;
    private String description;
    private String owner;
    private ArrayList<String> photos;
    private String birthDate;
    private ArrayList<Vaccination> vaccinationSchedule;
    private ArrayList<PetRequest> petRequests;

    public Pet(String uid, String uidFoundation, String description, String owner, ArrayList<String> photos, String birthDate, ArrayList<Vaccination> vaccinationSchedule, ArrayList<PetRequest> petRequests) {
        this.uid = uid;
        this.uidFoundation = uidFoundation;
        this.description = description;
        this.owner = owner;
        this.photos = photos;
        this.birthDate = birthDate;
        this.vaccinationSchedule = vaccinationSchedule;
        this.petRequests = petRequests;
    }

    public Pet() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidFoundation() {
        return uidFoundation;
    }

    public void setUidFoundation(String uidFoundation) {
        this.uidFoundation = uidFoundation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public ArrayList<Vaccination> getVaccinationSchedule() {
        return vaccinationSchedule;
    }

    public void setVaccinationSchedule(ArrayList<Vaccination> vaccinationSchedule) {
        this.vaccinationSchedule = vaccinationSchedule;
    }

    public ArrayList<PetRequest> getPetRequests() {
        return petRequests;
    }

    public void setPetRequests(ArrayList<PetRequest> petRequests) {
        this.petRequests = petRequests;
    }
}
