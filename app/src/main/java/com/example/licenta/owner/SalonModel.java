package com.example.licenta.owner;

public class SalonModel {
    private String salonName, salonState, salonCity, salonStreet, salonPostalCode, salonPhone, salonEmail, salonDescription;
    private String salonImage;

    public SalonModel() {
    }

    public SalonModel(String salonName, String salonState, String salonCity, String salonStreet, String salonPostalCode, String salonPhone, String salonEmail, String salonDescription, String salonImage) {
        this.salonName = salonName;
        this.salonState = salonState;
        this.salonCity = salonCity;
        this.salonStreet = salonStreet;
        this.salonPostalCode = salonPostalCode;
        this.salonPhone = salonPhone;
        this.salonEmail = salonEmail;
        this.salonDescription = salonDescription;
        this.salonImage = salonImage;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getSalonState() {
        return salonState;
    }

    public void setSalonState(String salonState) {
        this.salonState = salonState;
    }

    public String getSalonCity() {
        return salonCity;
    }

    public void setSalonCity(String salonCity) {
        this.salonCity = salonCity;
    }

    public String getSalonStreet() {
        return salonStreet;
    }

    public void setSalonStreet(String salonStreet) {
        this.salonStreet = salonStreet;
    }

    public String getSalonPostalCode() {
        return salonPostalCode;
    }

    public void setSalonPostalCode(String salonPostalCode) {
        this.salonPostalCode = salonPostalCode;
    }

    public String getSalonPhone() {
        return salonPhone;
    }

    public void setSalonPhone(String salonPhone) {
        this.salonPhone = salonPhone;
    }

    public String getSalonEmail() {
        return salonEmail;
    }

    public void setSalonEmail(String salonEmail) {
        this.salonEmail = salonEmail;
    }

    public String getSalonDescription() {
        return salonDescription;
    }

    public void setSalonDescription(String salonDescription) {
        this.salonDescription = salonDescription;
    }

    public String getSalonImage() {
        return salonImage;
    }

    public void setSalonImage(String salonImage) {
        this.salonImage = salonImage;
    }
}
