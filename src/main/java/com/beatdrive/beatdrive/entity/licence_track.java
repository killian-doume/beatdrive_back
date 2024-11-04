package com.beatdrive.beatdrive.entity;

import jakarta.validation.constraints.NotBlank;

public class Licence_track {

    private Integer id_licence_tracks;
    @NotBlank
    private String type;
    @NotBlank
    private String prix;
    private Tracks tracks;

    public Integer getId_licence_tracks() {
        return id_licence_tracks;
    }

    public void setId_licence_tracks(Integer id_licence_tracks) {
        this.id_licence_tracks = id_licence_tracks;
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

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    public Licence_track() {
    }

    public Licence_track(@NotBlank String type, @NotBlank String prix, Tracks tracks) {
        this.type = type;
        this.prix = prix;
        this.tracks = tracks;
    }

    public Licence_track(Integer id_licence_tracks, @NotBlank String type, @NotBlank String prix, Tracks tracks) {
        this.id_licence_tracks = id_licence_tracks;
        this.type = type;
        this.prix = prix;
        this.tracks = tracks;
    }

}
