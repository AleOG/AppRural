package com.proyecto.apprural.model.beans;

import java.math.BigDecimal;

public class Service {
    private String serviceId;
    private String name;
    private boolean included;
    private BigDecimal price;
    public Service() {
    }
    public Service(String serviceId, String name, boolean included, BigDecimal price) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
