package com.example.licenta.owner.others;

public class Salon {
    private String salonId;
    private String accountId;
    private String salonName;
    private String salonCounty, salonCity, salonStreet, salonPostalCode;
    private String salonPhone, salonEmail;
    private String salonDescription;
    private String salonImage;
    private String salonOpenHours;
    private String salonCloseHours;

    public Salon() {
    }

    public Salon(String salonId, String accountId, String salonName, String salonCounty, String salonCity, String salonStreet, String salonPostalCode, String salonPhone, String salonEmail, String salonDescription, String salonImage, String salonOpenHours, String salonCloseHours) {
        this.salonId = salonId;
        this.accountId = accountId;
        this.salonName = salonName;
        this.salonCounty = salonCounty;
        this.salonCity = salonCity;
        this.salonStreet = salonStreet;
        this.salonPostalCode = salonPostalCode;
        this.salonPhone = salonPhone;
        this.salonEmail = salonEmail;
        this.salonDescription = salonDescription;
        this.salonImage = salonImage;
        this.salonOpenHours = salonOpenHours;
        this.salonCloseHours = salonCloseHours;
    }

    public String getSalonOpenHours() {
        return salonOpenHours;
    }

    public void setSalonOpenHours(String salonOpenHours) {
        this.salonOpenHours = salonOpenHours;
    }

    public String getSalonCloseHours() {
        return salonCloseHours;
    }

    public void setSalonCloseHours(String salonCloseHours) {
        this.salonCloseHours = salonCloseHours;
    }

    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getSalonCounty() {
        return salonCounty;
    }

    public void setSalonCounty(String salonCounty) {
        this.salonCounty = salonCounty;
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
