package com.proyecto.apprural.model.beans;

import java.io.Serializable;

public class Bed implements Serializable {
    private String type;
    private int quantity;

    public Bed() {
    }

    public Bed(String type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Bed{" +
                "type='" + type + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
