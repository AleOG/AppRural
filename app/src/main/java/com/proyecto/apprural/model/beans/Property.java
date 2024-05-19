package com.proyecto.apprural.model.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Property {

    private String propertyId;
    private String name;
    private String address;
    private String BookingType;
    private boolean published;
    private boolean validated;
    private BigDecimal price;
    private List<Service> services = new ArrayList<>();
    private List<Prohibition> prohibitions = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private String ownerId;

    public Property() {
    }

    public Property(String propertyId, String name, String address, String bookingType, boolean published,
                    boolean validated, BigDecimal price, List<Service> services, List<Prohibition> prohibitions,
                    List<Room> rooms, String ownerId) {
        this.propertyId = propertyId;
        this.name = name;
        this.address = address;
        BookingType = bookingType;
        this.published = published;
        this.validated = validated;
        this.price = price;
        this.services = services;
        this.prohibitions = prohibitions;
        this.rooms = rooms;
        this.ownerId = ownerId;
    }
}
