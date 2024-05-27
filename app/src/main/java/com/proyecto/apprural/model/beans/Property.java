package com.proyecto.apprural.model.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Property implements Serializable {

    private String propertyId;
    private String name;
    private String address;
    private String bookingType;
    private boolean published;
    private boolean validated;
    private double price;
    private List<Service> services = new ArrayList<>();
    private List<Prohibition> prohibitions = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private String ownerId;

    public Property() {
    }

    public Property(String propertyId, String name, String address, String bookingType, boolean published,
                    boolean validated, double price, List<Service> services, List<Prohibition> prohibitions,
                    List<Room> rooms, String ownerId) {
        this.propertyId = propertyId;
        this.name = name;
        this.address = address;
        this.bookingType = bookingType;
        this.published = published;
        this.validated = validated;
        this.price = price;
        this.services = services;
        this.prohibitions = prohibitions;
        this.rooms = rooms;
        this.ownerId = ownerId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
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

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
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

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Property{" +
                "propertyId='" + propertyId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", bookingType='" + bookingType + '\'' +
                ", published=" + published +
                ", validated=" + validated +
                ", price=" + price +
                ", services=" + services +
                ", prohibitions=" + prohibitions +
                ", rooms=" + rooms +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}
