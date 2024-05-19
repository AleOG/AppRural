package com.proyecto.apprural.model.beans;

import java.time.LocalDateTime;

public class Guest extends User{

    private String name;
    private String firstLastName;
    private String secondLastName;
    private String dni;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private String creditCardNumber;
    private boolean member;
    private String memberCategory;
    private int fidelityPoints;

    public Guest() {
    }

    public Guest(String userId, String password, String email, String role, LocalDateTime registerDate,
                 String name, String firstLastName, String secondLastName, String dni, String phoneNumber,
                 LocalDateTime dateOfBirth, String creditCardNumber, boolean member, String memberCategory,
                 int fidelityPoints) {
        super(userId, password, email, role, registerDate);
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.dni = dni;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.creditCardNumber = creditCardNumber;
        this.member = member;
        this.memberCategory = memberCategory;
        this.fidelityPoints = fidelityPoints;
    }
}
