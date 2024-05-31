package com.proyecto.apprural.model.beans;

import java.io.Serializable;
import java.util.List;

public class Offer implements Serializable {
    private String idOwner;
    private boolean published;
    private boolean validated;
    private String name;
    private String address;
    private String town;
    private String country;
    private String offerType;
    private double price;
    private List<Service> services;
    private List<Prohibition> prohibitions;
    private int capacity;


    public Offer() {
    }

    public Offer(String idOwner, boolean published, boolean validated, String name,
                 String address, String town, String country, String offerType, double price, List<Service> services,
                 List<Prohibition> prohibitions, int capacity) {
        this.idOwner = idOwner;
        this.published = published;
        this.validated = validated;
        this.name = name;
        this.address = address;
        this.town = town;
        this.country = country;
        this.offerType = offerType;
        this.price = price;
        this.services = services;
        this.prohibitions = prohibitions;
        this.capacity = capacity;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    @Override
    public String toString() {
        return "Offer{" +
                "idOwner='" + idOwner + '\'' +
                ", published=" + published +
                ", validated=" + validated +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", town='" + town + '\'' +
                ", country='" + country + '\'' +
                ", offerType='" + offerType + '\'' +
                ", price=" + price +
                ", services=" + services +
                ", prohibitions=" + prohibitions +
                ", capacity=" + capacity +
                '}';
    }
}
