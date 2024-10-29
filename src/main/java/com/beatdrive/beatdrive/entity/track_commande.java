package com.beatdrive.beatdrive.entity;

import java.util.List;
import java.util.ArrayList;

public class track_commande {

    private Integer track_commande;
    private List<track> tracks = new ArrayList<>();
    private List<details_commande> details = new ArrayList<>();

    public Integer getTrack_commande() {
        return track_commande;
    }

    public void setTrack_commande(Integer track_commande) {
        this.track_commande = track_commande;
    }

    public List<track> getTracks() {
        return tracks;
    }

    public void setTracks(List<track> tracks) {
        this.tracks = tracks;
    }

    public List<details_commande> getDetails() {
        return details;
    }

    public void setDetails(List<details_commande> details) {
        this.details = details;
    }

    public track_commande() {
    }

    public track_commande(List<track> tracks, List<details_commande> details) {
        this.tracks = tracks;
        this.details = details;
    }

    public track_commande(Integer track_commande, List<track> tracks, List<details_commande> details) {
        this.track_commande = track_commande;
        this.tracks = tracks;
        this.details = details;
    }
}
