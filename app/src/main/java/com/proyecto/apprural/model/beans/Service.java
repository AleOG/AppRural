package com.proyecto.apprural.model.beans;

import java.io.Serializable;

public class Service implements Serializable {
    private String serviceId;
    private String name;
    private boolean included;
    private double price;
    public Service() {
    }
    public Service(String serviceId, String name, boolean included, double price) {
        this.serviceId = serviceId;
        this.name = name;
        this.included = included;
        this.price = price;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId='" + serviceId + '\'' +
                ", name='" + name + '\'' +
                ", included=" + included +
                ", price=" + price +
                '}';
    }
}
