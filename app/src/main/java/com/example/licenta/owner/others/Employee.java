package com.example.licenta.owner.others;

public class Employee {
    private String employeeId;
    private String userType;
    private String ownerId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String salonId;
    private String department;
    private Salon salon;
    private String nivelPregatire;

    public Employee() {
    }

    public Employee(String employeeId, String userType, String ownerId, String firstName, String lastName, String phoneNumber,
                    String email, String salonId, String department, String nivelPregatire) {
        this.employeeId = employeeId;
        this.userType = userType;
        this.ownerId = ownerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.salonId = salonId;
        this.department = department;
        this.nivelPregatire = nivelPregatire;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Salon getSalon() {
        return salon;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public String getNivelPregatire() {
        return nivelPregatire;
    }

    public void setNivelPregatire(String nivelPregatire) {
        this.nivelPregatire = nivelPregatire;
    }
}
