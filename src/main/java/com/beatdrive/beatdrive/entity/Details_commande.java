package com.beatdrive.beatdrive.entity;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class Details_commande {
    private int id_detail_commande;
    @NotBlank
    private String prix_total;
    @NotBlank
    private String nombre_total;
    private LocalDate date;
    private User users;

    public int getId_detail_commande() {
        return id_detail_commande;
    }

    public void setId_detail_commande(int id_detail_commande) {
        this.id_detail_commande = id_detail_commande;
    }

    public String getPrix_total() {
        return prix_total;
    }

    public void setPrix_total(String prix_total) {
        this.prix_total = prix_total;
    }

    public String getNombre_total() {
        return nombre_total;
    }

    public void setNombre_total(String nombre_total) {
        this.nombre_total = nombre_total;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public Details_commande() {
    }

    public Details_commande(@NotBlank String prix_total, @NotBlank String nombre_total, LocalDate date, User users) {
        this.prix_total = prix_total;
        this.nombre_total = nombre_total;
        this.date = date;
        this.users = users;
    }

    public Details_commande(int id_detail_commande, @NotBlank String prix_total, @NotBlank String nombre_total,
            LocalDate date, User users) {
        this.id_detail_commande = id_detail_commande;
        this.prix_total = prix_total;
        this.nombre_total = nombre_total;
        this.date = date;
        this.users = users;
    }
}
