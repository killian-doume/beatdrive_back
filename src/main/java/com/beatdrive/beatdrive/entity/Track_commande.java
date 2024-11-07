package com.beatdrive.beatdrive.entity;

public class Track_commande {
    private Integer id_track_commande;
    private Licence_track Licence_track;
    private Detail_commande Detail_commande;

    public Integer getId_track_commande() {
        return id_track_commande;
    }

    public void setId_track_commande(Integer id_track_commande) {
        this.id_track_commande = id_track_commande;
    }

    public Licence_track getLicence_track() {
        return Licence_track;
    }

    public void setLicence_track(Licence_track licence_track) {
        Licence_track = licence_track;
    }

    public Detail_commande getDetail_commande() {
        return Detail_commande;
    }

    public void setDetail_commande(Detail_commande detail_commande) {
        Detail_commande = detail_commande;
    }

    public Track_commande() {
    }

    public Track_commande(com.beatdrive.beatdrive.entity.Licence_track licence_track,
            com.beatdrive.beatdrive.entity.Detail_commande detail_commande) {
        Licence_track = licence_track;
        Detail_commande = detail_commande;
    }

    public Track_commande(Integer id_track_commande, com.beatdrive.beatdrive.entity.Licence_track licence_track,
            com.beatdrive.beatdrive.entity.Detail_commande detail_commande) {
        this.id_track_commande = id_track_commande;
        Licence_track = licence_track;
        Detail_commande = detail_commande;
    }

}
