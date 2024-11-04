package com.beatdrive.beatdrive.entity;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class Tracks {

    private Integer id_tracks;
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
    private String status;
    private String like;
    @NotBlank
    private String cover;
    private User users;

    public Integer getId_tracks() {
        return id_tracks;
    }

    public void setId_tracks(Integer id_tracks) {
        this.id_tracks = id_tracks;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public Tracks(Integer id_tracks, @NotBlank String titre, LocalDateTime date, @NotBlank String bpm,
            String description, @NotBlank String cle, @NotBlank String genre, @NotBlank String type,
            @NotBlank String audio, @NotBlank String status, String like, @NotBlank String cover, User users) {
        this.id_tracks = id_tracks;
        this.titre = titre;
        this.date = date;
        this.bpm = bpm;
        this.description = description;
        this.cle = cle;
        this.genre = genre;
        this.type = type;
        this.audio = audio;
        this.status = status;
        this.like = like;
        this.cover = cover;
        this.users = users;
    }

    public Tracks(@NotBlank String titre, LocalDateTime date, @NotBlank String bpm, String description,
            @NotBlank String cle, @NotBlank String genre, @NotBlank String type, @NotBlank String audio,
            @NotBlank String status, String like, @NotBlank String cover, User users) {
        this.titre = titre;
        this.date = date;
        this.bpm = bpm;
        this.description = description;
        this.cle = cle;
        this.genre = genre;
        this.type = type;
        this.audio = audio;
        this.status = status;
        this.like = like;
        this.cover = cover;
        this.users = users;
    }

    public Tracks() {
    }

}