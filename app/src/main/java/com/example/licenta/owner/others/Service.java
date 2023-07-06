package com.example.licenta.owner.others;

public class Service {
    private String serviceName;
    private String servicePrice;
    private String serviceDepartment;
    private String serviceDescription;

    public Service() {
    }

    public Service(String serviceName, String servicePrice, String serviceDepartment, String serviceDescription) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceDepartment = serviceDepartment;
        this.serviceDescription = serviceDescription;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceDepartment() {
        return serviceDepartment;
    }

    public void setServiceDepartment(String serviceDepartment) {
        this.serviceDepartment = serviceDepartment;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
}
