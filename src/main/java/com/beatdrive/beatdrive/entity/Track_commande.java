package com.beatdrive.beatdrive.entity;

public class Track_commande {
    private Integer id_track_commande;
    private Licence_track Licence_tracks;
    private Detail_commande Details_commande;

    public Integer getId_track_commande() {
        return id_track_commande;
    }

    public void setId_track_commande(Integer id_track_commande) {
        this.id_track_commande = id_track_commande;
    }

    public Licence_track getLicence_tracks() {
        return Licence_tracks;
    }

    public void setLicence_tracks(Licence_track licence_tracks) {
        Licence_tracks = licence_tracks;
    }

    public Detail_commande getDetails_commande() {
        return Details_commande;
    }

    public void setDetails_commande(Detail_commande details_commande) {
        Details_commande = details_commande;
    }

    public Track_commande(Licence_track licence_tracks, Detail_commande details_commande) {
        Licence_tracks = licence_tracks;
        Details_commande = details_commande;
    }

    public Track_commande() {
    }

    public Track_commande(Integer id_track_commande, Licence_track licence_tracks, Detail_commande details_commande) {
        this.id_track_commande = id_track_commande;
        Licence_tracks = licence_tracks;
        Details_commande = details_commande;
    }

}
