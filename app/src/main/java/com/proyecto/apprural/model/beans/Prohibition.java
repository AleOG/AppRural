package com.proyecto.apprural.model.beans;

import java.io.Serializable;

public class Prohibition implements Serializable {
    private String prohibitionId;

    private String name;

    public Prohibition() {
    }

    public Prohibition(String id, String name) {
        this.prohibitionId = id;
        this.name = name;
    }

    public String getProhibitionId() {
        return prohibitionId;
    }

    public void setProhibitionId(String prohibitionId) {
        this.prohibitionId = prohibitionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
