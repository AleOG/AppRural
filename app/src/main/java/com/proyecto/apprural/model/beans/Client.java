package com.proyecto.apprural.model.beans;

import android.os.Build;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Client extends User implements Serializable {
    private String name;
    private String subRole;
    private String firstLastName;
    private String secondLastName;
    private String dni;
    private String phoneNumber;
    private String dateOfBirth;
    private String creditCardNumber; //atributo de guest
    private boolean member; //atributo de guest
    private String memberCategory; //atributo de guest
    private int fidelityPoints; //atributo de guest
    private int numberOfProperties; //atributo de owner
    private String bankAccountNumber; //atributo de owner

    public Client() {
    }

    public Client(String userId, String password, String email, String role, String registerDate,
                  String name, String subRole, String firstLastName, String secondLastName, String dni,
                  String phoneNumber, String dateOfBirth, String creditCardNumber, boolean member,
                  String memberCategory, int fidelityPoints, int numberOfProperties, String bankAccountNumber) {
        super(userId, password, email, role, registerDate);
        this.name = name;
        this.subRole = subRole;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.dni = dni;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.creditCardNumber = creditCardNumber;
        this.member = member;
        this.memberCategory = memberCategory;
        this.fidelityPoints = fidelityPoints;
        this.numberOfProperties = numberOfProperties;
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubRole() {
        return subRole;
    }

    public void setSubRole(String subRole) {
        this.subRole = subRole;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
            return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public String getMemberCategory() {
        return memberCategory;
    }

    public void setMemberCategory(String memberCategory) {
        this.memberCategory = memberCategory;
    }

    public int getFidelityPoints() {
        return fidelityPoints;
    }

    public void setFidelityPoints(int fidelityPoints) {
        this.fidelityPoints = fidelityPoints;
    }

    public int getNumberOfProperties() {
        return numberOfProperties;
    }

    public void setNumberOfProperties(int numberOfProperties) {
        this.numberOfProperties = numberOfProperties;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", firstLastName='" + firstLastName + '\'' +
                ", secondLastName='" + secondLastName + '\'' +
                ", dni='" + dni + '\'' +
                "email" +getEmail() + '\'' +
                "password" + getPassword() + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", member=" + member +
                ", memberCategory='" + memberCategory + '\'' +
                ", fidelityPoints=" + fidelityPoints +
                ", numberOfProperties=" + numberOfProperties +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                '}';
    }
}
