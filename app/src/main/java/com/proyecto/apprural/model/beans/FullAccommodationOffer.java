package com.proyecto.apprural.model.beans;

import java.io.Serializable;
import java.util.List;

public class FullAccommodationOffer extends Offer implements Serializable {

    private String idProperty;
    private List<Room> rooms;

    public FullAccommodationOffer() {
    }

    public FullAccommodationOffer(String idOwner, boolean published, boolean validated, String name,
                                  String address, String town, String country, String offerType, double price,
                                  List<Service> services, List<Prohibition> prohibitions, int capacity, String idProperty,
                                  List<Room> rooms) {
        super(idOwner, published, validated, name, address, town, country, offerType, price, services, prohibitions, capacity);
        this.idProperty = idProperty;
        this.rooms = rooms;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "FullAccommodationOffer{" +
                "idOwner='" + getIdOwner() + '\'' +
                ", published=" + isPublished() +
                ", validated=" + isValidated() +
                ", name='" + getName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", town='" + getTown() + '\'' +
                ", country='" + getCountry() + '\'' +
                ", offerType='" + getOfferType() + '\'' +
                ", price=" + getPrice() +
                ", services=" + getServices() +
                ", prohibitions=" + getProhibitions() +
                ", capacity=" + getCapacity() +
                ", idProperty='" + idProperty + '\'' +
                ", rooms=" + rooms +
                '}';
    }

}
