package com.beatdrive.beatdrive.entity;

import java.util.List;
import java.util.ArrayList;

public class details_commande {

    private Integer details_commande;
    private String prix_total;
    private String nombre_total;
    private List<account> accounts = new ArrayList<>();
    private List<historique_commande> historique_commandes = new ArrayList<>();

    public Integer getDetails_commande() {
        return details_commande;
    }

    public void setDetails_commande(Integer details_commande) {
        this.details_commande = details_commande;
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

    public List<account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<account> accounts) {
        this.accounts = accounts;
    }

    public List<historique_commande> getHistorique_commandes() {
        return historique_commandes;
    }

    public void setHistorique_commandes(List<historique_commande> historique_commandes) {
        this.historique_commandes = historique_commandes;
    }

    public details_commande(String prix_total, String nombre_total, List<account> accounts,
            List<historique_commande> historique_commandes) {
        this.prix_total = prix_total;
        this.nombre_total = nombre_total;
        this.accounts = accounts;
        this.historique_commandes = historique_commandes;
    }

    public details_commande() {
    }

    public details_commande(Integer details_commande, String prix_total, String nombre_total, List<account> accounts,
            List<historique_commande> historique_commandes) {
        this.details_commande = details_commande;
        this.prix_total = prix_total;
        this.nombre_total = nombre_total;
        this.accounts = accounts;
        this.historique_commandes = historique_commandes;
    }

}
