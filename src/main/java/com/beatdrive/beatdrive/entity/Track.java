package com.beatdrive.beatdrive.entity;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class Track {

    private Integer id_track;
    @NotBlank
    private String titre;

    private LocalDate date;

    private String bpm;
    private String description;

    private String cle;

    private String genre;

    private String type;

    private String audio;

    private String statut;
    @NotBlank
    private String cover;
    private Integer id_user; // au lieu de l'objet User

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public Track() {
    }

    public Track(@NotBlank String titre, LocalDate date, String bpm, String description, String cle, String genre,
            String type, String audio, String statut, @NotBlank String cover, Integer id_user) {
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
        this.id_user = id_user;
    }

    public Track(Integer id_track, @NotBlank String titre, LocalDate date, String bpm, String description, String cle,
            String genre, String type, String audio, String statut, @NotBlank String cover, Integer id_user) {
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
        this.id_user = id_user;
    }
}