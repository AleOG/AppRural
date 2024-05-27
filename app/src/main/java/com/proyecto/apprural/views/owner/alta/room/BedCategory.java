package com.proyecto.apprural.views.owner.alta.room;

public enum BedCategory {
    INDIVIDUAL("Individual"),
    LITERA("Litera"),
    SOFA_CAMA("Sofa cama"),
    CUNA("Cuna"),
    MATRIMONIO("Matrimonio");

    private String bedCategory;
    BedCategory(String bedCategory) {
        this.bedCategory = bedCategory;
    }

    public String getBedCategory() {
        return bedCategory;
    }

    @Override
    public String toString() {
        return bedCategory;
    }
}
