package com.myapp.ui;

import java.util.Date;

public class Utilisateur {
    private int id;
    private String email;
    private String password;
    private Date inscriptionDate;
    private String name;
    private boolean status;
    private static Utilisateur currentUser;

    public Utilisateur(int id, String email, String password, Date inscriptionDate, String name, boolean status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.inscriptionDate = inscriptionDate;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getInscriptionDate() {
        return inscriptionDate;
    }

    public String getName() {
        return name;
    }

    public boolean getStatus() {
        return status;
    }

    // Méthode pour définir l'utilisateur actuellement connecté
    public static void setCurrentUser(Utilisateur user) {
        currentUser = user;
    }

    // Méthode pour obtenir l'utilisateur actuellement connecté
    public static Utilisateur getCurrentUser() {
        return currentUser;
    }

}
