package com.beatdrive.beatdrive.entity;

import java.time.LocalDate;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Track(@NotBlank String titre, LocalDate date, @NotBlank String bpm, String description,
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

    public Track(Integer id_track, @NotBlank String titre, LocalDate date, @NotBlank String bpm, String description,
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

    public Track() {
    }

    public String getLink() {
        if (cover.startsWith("http")) {
            return cover;
        }
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return baseUrl + "/uploads/" + cover;
    }

    public String getThumbnailLink() {
        if (cover.startsWith("http")) {
            return cover;
        }
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return baseUrl + "/uploads/thumbnail-" + cover;
    }

}