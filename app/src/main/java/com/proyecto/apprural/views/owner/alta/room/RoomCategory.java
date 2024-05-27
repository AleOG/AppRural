package com.proyecto.apprural.views.owner.alta.room;

public enum RoomCategory {
    LOW_COST("Low_cost"),
    STANDARD("Standard"),
    PREMIUM("Premium"),
    SUITE("Suite");

    private String roomCategory;

    RoomCategory(String roomCategory) {
        this.roomCategory = roomCategory;
    }

    public String getRoomCategory() {
        return roomCategory;
    }

    @Override
    public String toString() {
        return roomCategory;
    }
}
