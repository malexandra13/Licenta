package com.example.licenta.client.others;

import android.icu.text.DateFormat;

import java.sql.Time;
import java.util.Date;

public class Appointment {
    private String salonId;
    private String clientId;
    private String employeeId;
    private String serviceId;
    private String price;
    private String date;
    private String time;

    public Appointment() {
    }

    public Appointment(String salonId, String clientId, String employeeId, String serviceId, String price, String date, String time) {
        this.salonId = salonId;
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.employeeId = employeeId;
        this.price = price;
        this.date = date;
        this.time = time;
    }


    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String service) {
        this.serviceId = service;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "salonId='" + salonId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", price='" + price + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    public void setTime(String time) {
        this.time = time;
    }
}
