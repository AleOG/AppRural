package com.proyecto.apprural.model.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Reservation implements Serializable {
    private String reservationCode;
    private String checkin;
    private String checkout;
    private int numberOfPeople;
    private String status;
    private double price;
    private String creationDate;
    private String guestId;
    private String guestName;
    private String ownerId;
    private String accomodationId;
    private String bookingType;
    private List<Room> rooms = new ArrayList<>();
    private List<Service> extraServicesProperty = new ArrayList<>();
    private List<Service> extraServicesRooms = new ArrayList<>();

    public Reservation() {
    }
    public Reservation(String reservationCode, String checkin, String checkout,
                       int numberOfPeople, String status, double price, String creationDate,
                       String guestId, String guestName, String ownerId, String accomodationId, String bookingType,
                       List<Room> rooms, List<Service> extraServicesProperty, List<Service> extraServicesRooms) {
        this.reservationCode = reservationCode;
        this.checkin = checkin;
        this.checkout = checkout;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
        this.price = price;
        this.creationDate = creationDate;
        this.guestId = guestId;
        this.guestName = guestName;
        this.ownerId = ownerId;
        this.accomodationId = accomodationId;
        this.bookingType = bookingType;
        this.rooms = rooms;
        this.extraServicesProperty = extraServicesProperty;
        this.extraServicesRooms = extraServicesRooms;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAccomodationId() {
        return accomodationId;
    }

    public void setAccomodationId(String accomodationId) {
        this.accomodationId = accomodationId;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Service> getExtraServicesProperty() {
        return extraServicesProperty;
    }

    public void setExtraServicesProperty(List<Service> extraServicesProperty) {
        this.extraServicesProperty = extraServicesProperty;
    }

    public List<Service> getExtraServicesRooms() {
        return extraServicesRooms;
    }

    public void setExtraServicesRooms(List<Service> extraServicesRooms) {
        this.extraServicesRooms = extraServicesRooms;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationCode='" + reservationCode + '\'' +
                ", checkin='" + checkin + '\'' +
                ", checkout='" + checkout + '\'' +
                ", numberOfPeople=" + numberOfPeople +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", creationDate='" + creationDate + '\'' +
                ", guestId='" + guestId + '\'' +
                ", guestName='" + guestName + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", accomodationId='" + accomodationId + '\'' +
                ", bookingType='" + bookingType + '\'' +
                ", rooms=" + rooms +
                ", extraServicesProperty=" + extraServicesProperty +
                ", extraServicesRooms=" + extraServicesRooms +
                '}';
    }
}
