package com.beatdrive.beatdrive.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class track {

    private Integer id_track;
    private String titre;
    private LocalDateTime date;
    private String bpm;
    private String description;
    private String cle;
    private String prix;
    private String genre;
    private String type;
    private String audio;
    private String status;
    private String like;
    private String cover;
    private List<account> accounts = new ArrayList<>();

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

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
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

    public List<account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<account> accounts) {
        this.accounts = accounts;
    }

    public track(String titre, LocalDateTime date, String bpm, String description, String cle, String prix,
            String genre, String type, String audio, String status, String like, String cover, List<account> accounts) {
        this.titre = titre;
        this.date = date;
        this.bpm = bpm;
        this.description = description;
        this.cle = cle;
        this.prix = prix;
        this.genre = genre;
        this.type = type;
        this.audio = audio;
        this.status = status;
        this.like = like;
        this.cover = cover;
        this.accounts = accounts;
    }

    public track() {
    }

    public track(Integer id_track, String titre, LocalDateTime date, String bpm, String description, String cle,
            String prix, String genre, String type, String audio, String status, String like, String cover,
            List<account> accounts) {
        this.id_track = id_track;
        this.titre = titre;
        this.date = date;
        this.bpm = bpm;
        this.description = description;
        this.cle = cle;
        this.prix = prix;
        this.genre = genre;
        this.type = type;
        this.audio = audio;
        this.status = status;
        this.like = like;
        this.cover = cover;
        this.accounts = accounts;
    }

}
