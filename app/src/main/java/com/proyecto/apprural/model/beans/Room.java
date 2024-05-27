package com.proyecto.apprural.model.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private String roomId;

    private String name;
    private String category;
    private boolean published;
    private double price;
    private String status;
    private List<Service> services = new ArrayList<>();
    private List<Bed> beds = new ArrayList<>();

    public Room() {
    }
    public Room(String roomId, String name, String category, boolean published, double price, String status,
                List<Service> services, List<Bed> beds) {
        this.roomId = roomId;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", published=" + published +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", services=" + services +
                ", beds=" + beds +
                '}';
    }
}
