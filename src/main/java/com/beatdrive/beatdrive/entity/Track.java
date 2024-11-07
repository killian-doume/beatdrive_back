package com.beatdrive.beatdrive.entity;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class Track {

    private Integer id_track;
    @NotBlank
    private String titre;
    private LocalDateTime date;
    @NotBlank
    private String bpm;
    private String description;
    @NotBlank
    private String cle;
    @NotBlank
    private String genre;
    @NotBlank
    private String type;
    @NotBlank
    private String audio;
    @NotBlank
    private String statut;
    @NotBlank
    private String cover;
    private User user;

    public Integer getId_track() {
        return id_track;
    }

    public void setId_track(Integer id_track) {
        this.id_track = id_track;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getBpm() {
        return bpm;
    }

    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Track() {
    }

    public Track(@NotBlank String titre, LocalDateTime date, @NotBlank String bpm, String description,
            @NotBlank String cle, @NotBlank String genre, @NotBlank String type, @NotBlank String audio,
            @NotBlank String statut, @NotBlank String cover, User user) {
        this.titre = titre;
        this.date = date;
        this.bpm = bpm;
        this.description = description;
        this.cle = cle;
        this.genre = genre;
        this.type = type;
        this.audio = audio;
        this.statut = statut;
        this.cover = cover;
        this.user = user;
    }

    public Track(Integer id_track, @NotBlank String titre, LocalDateTime date, @NotBlank String bpm, String description,
            @NotBlank String cle, @NotBlank String genre, @NotBlank String type, @NotBlank String audio,
            @NotBlank String statut, @NotBlank String cover, User user) {
        this.id_track = id_track;
        this.titre = titre;
        this.date = date;
        this.bpm = bpm;
        this.description = description;
        this.cle = cle;
        this.genre = genre;
        this.type = type;
        this.audio = audio;
        this.statut = statut;
        this.cover = cover;
        this.user = user;
    }

}