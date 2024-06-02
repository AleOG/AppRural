package com.proyecto.apprural.model.beans;

import java.time.LocalDateTime;

public class Admin extends User {

    public Admin() {
    }

    public Admin(String userId, String password, String email, String role, String registerDate) {
        super(userId, password, email, role, registerDate);
    }
}
