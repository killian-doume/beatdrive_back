package com.beatdrive.beatdrive.entity;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

public class licence_track {

    private Integer id_licence_track;
    @NotBlank
    private String type;
    private List<track> tracks = new ArrayList<>();

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

    public List<track> getTracks() {
        return tracks;
    }

    public void setTracks(List<track> tracks) {
        this.tracks = tracks;
    }

    public licence_track() {
    }

    public licence_track(String type, List<track> tracks) {
        this.type = type;
        this.tracks = tracks;
    }

    public licence_track(Integer id_licence_track, String type, List<track> tracks) {
        this.id_licence_track = id_licence_track;
        this.type = type;
        this.tracks = tracks;
    }

}
