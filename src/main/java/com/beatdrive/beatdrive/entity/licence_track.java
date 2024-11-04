package com.beatdrive.beatdrive.entity;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

public class Licence_track {

    private Integer id_licence_track;
    @NotBlank
    private String type;
    @NotBlank
    private String prix;
    private Track tracks;

}
