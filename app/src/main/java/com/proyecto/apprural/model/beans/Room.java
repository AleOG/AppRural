package com.proyecto.apprural.model.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomId;
    private String category;
    private boolean published;
    private BigDecimal price;
    private String status;
    private List<Service> services = new ArrayList<>();
    private List<Bed> beds = new ArrayList<>();

    public Room() {
    }
    public Room(String roomId, String category, boolean published, BigDecimal price, String status,
                List<Service> services, List<Bed> beds) {
        this.roomId = roomId;
        this.category = category;
        this.published = published;
        this.price = price;
        this.status = status;
        this.services = services;
        this.beds = beds;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    public void setBeds(List<Bed> beds) {
        this.beds = beds;
    }
}
