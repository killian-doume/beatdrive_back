
package com.beatdrive.beatdrive.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class historique_commande {
    private Integer id_historique_commande;
    private LocalDateTime date;
    private String prix;
    private List<account> accounts = new ArrayList<>();

    public Integer getId_historique_commande() {
        return id_historique_commande;
    }

    public void setId_historique_commande(Integer id_historique_commande) {
        this.id_historique_commande = id_historique_commande;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public List<account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<account> accounts) {
        this.accounts = accounts;
    }

    public historique_commande() {
    }

    public historique_commande(LocalDateTime date, String prix, List<account> accounts) {
        this.date = date;
        this.prix = prix;
        this.accounts = accounts;
    }

    public historique_commande(Integer id_historique_commande, LocalDateTime date, String prix,
            List<account> accounts) {
        this.id_historique_commande = id_historique_commande;
        this.date = date;
        this.prix = prix;
        this.accounts = accounts;
    }
}
