package com.proyecto.apprural.model.beans;

import java.time.LocalDateTime;

public class Owner extends User{

    private String name;
    private String firstLastName;
    private String secondLastName;
    private String dni;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private int numberOfProperties;
    private String bankAccountNumber;

    public Owner() {
    }

    public Owner(String userId, String password, String email, String role, LocalDateTime registerDate,
                 String name, String firstLastName, String secondLastName, String dni, String phoneNumber,
                 LocalDateTime dateOfBirth, int numberOfProperties, String bankAccountNumber) {
        super(userId, password, email, role, registerDate);
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.dni = dni;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.numberOfProperties = numberOfProperties;
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
}
