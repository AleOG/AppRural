package com.proyecto.apprural.model.beans;

import java.io.Serializable;
import java.util.List;

public class RoomAccommodationOffer extends Offer implements Serializable {
    private String idRoom;
    private String idProperty;
    private String category;
    private String status;
    private List<Bed> beds;

    public RoomAccommodationOffer() {
    }

    public RoomAccommodationOffer(String idOwner, boolean published, boolean validated, String name,
                                  String address, String town, String country, String offerType, double price,
                                  List<Service> services, List<Prohibition> prohibitions, int capacity, String idRoom,
                                  String idProperty, String category, String status, List<Bed> beds) {
        super(idOwner, published, validated, name, address, town, country, offerType, price, services, prohibitions, capacity);
        this.idRoom = idRoom;
        this.idProperty = idProperty;
        this.category = category;
        this.status = status;
        this.beds = beds;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    public void setBeds(List<Bed> beds) {
        this.beds = beds;
    }

    @Override
    public String toString() {
        return "RoomAccommodationOffer{" +
                "idRoom='" + idRoom + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", beds=" + beds +
                '}';
    }
}
