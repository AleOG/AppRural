package com.proyecto.apprural.model.beans;

import java.io.Serializable;
import java.util.List;

public class FullAccommodationOffer extends Offer implements Serializable {

    private String idProperty;
    private List<Room> rooms;

    public FullAccommodationOffer() {
    }

    public FullAccommodationOffer(String idOwner, boolean published, boolean validated, String name,
                                  String location, String offerType, double price, List<Service> services,
                                  List<Prohibition> prohibitions, String idProperty, List<Room> rooms) {
        super(idOwner, published, validated, name, location, offerType, price, services, prohibitions);
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
                "idProperty='" + idProperty + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
