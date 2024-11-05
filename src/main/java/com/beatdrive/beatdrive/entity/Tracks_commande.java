package com.beatdrive.beatdrive.entity;

public class Tracks_commande {
    private Integer id_tracks_commande;
    private Licence_track Licence_tracks;
    private Details_commande Details_commande;

    public Integer getId_tracks_commande() {
        return id_tracks_commande;
    }

    public void setId_tracks_commande(Integer id_tracks_commande) {
        this.id_tracks_commande = id_tracks_commande;
    }

    public Licence_track getLicence_tracks() {
        return Licence_tracks;
    }

    public void setLicence_tracks(Licence_track licence_tracks) {
        Licence_tracks = licence_tracks;
    }

    public Details_commande getDetails_commande() {
        return Details_commande;
    }

    public void setDetails_commande(Details_commande details_commande) {
        Details_commande = details_commande;
    }

    public Tracks_commande() {
    }

    public Tracks_commande(Licence_track licence_tracks,
            com.beatdrive.beatdrive.entity.Details_commande details_commande) {
        Licence_tracks = licence_tracks;
        Details_commande = details_commande;
    }

    public Tracks_commande(Integer id_tracks_commande, Licence_track licence_tracks,
            com.beatdrive.beatdrive.entity.Details_commande details_commande) {
        this.id_tracks_commande = id_tracks_commande;
        Licence_tracks = licence_tracks;
        Details_commande = details_commande;
    }
}
