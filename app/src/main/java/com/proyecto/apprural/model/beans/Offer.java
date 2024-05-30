package com.proyecto.apprural.model.beans;

import java.util.List;

public class Offer {
    private String idOwner;
    private boolean published;
    private boolean validated;
    private String name;
    private String location;
    private String offerType;
    private double price;
    private List<Service> services;
    private List<Prohibition> prohibitions;

    public Offer() {
    }

    public Offer(String idOwner, boolean published, boolean validated, String name,
                 String location, String offerType, double price, List<Service> services,
                 List<Prohibition> prohibitions) {
        this.idOwner = idOwner;
        this.published = published;
        this.validated = validated;
        this.name = name;
        this.location = location;
        this.offerType = offerType;
        this.price = price;
        this.services = services;
        this.prohibitions = prohibitions;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Prohibition> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<Prohibition> prohibitions) {
        this.prohibitions = prohibitions;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "idOwner='" + idOwner + '\'' +
                ", published=" + published +
                ", validated=" + validated +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", offerType='" + offerType + '\'' +
                ", price=" + price +
                ", services=" + services +
                ", prohibitions=" + prohibitions +
                '}';
    }
}
