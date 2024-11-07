package com.beatdrive.beatdrive.entity;

import jakarta.validation.constraints.NotBlank;

public class Licence_track {

    private Integer id_licence_track;
    @NotBlank
    private String type;
    @NotBlank
    private String prix;
    private Track track;

    public Integer getId_licence_track() {
        return id_licence_track;
    }

    public void setId_licence_track(Integer id_licence_track) {
        this.id_licence_track = id_licence_track;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Licence_track() {
    }

    public Licence_track(@NotBlank String type, @NotBlank String prix, Track track) {
        this.type = type;
        this.prix = prix;
        this.track = track;
    }

    public Licence_track(Integer id_licence_track, @NotBlank String type, @NotBlank String prix, Track track) {
        this.id_licence_track = id_licence_track;
        this.type = type;
        this.prix = prix;
        this.track = track;
    }

}
