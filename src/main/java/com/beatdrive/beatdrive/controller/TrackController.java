package com.beatdrive.beatdrive.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.beatdrive.beatdrive.entity.Track;
import com.beatdrive.beatdrive.entity.User;
import com.beatdrive.beatdrive.repository.TrackRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
public class TrackController {

    @Autowired
    private TrackRepo trackRepo;

    @GetMapping("/api/track")
    public List<Track> getAllTracks() {
        return trackRepo.findAll();
    }

    @GetMapping("/api/track/{id}")
    public Track getTrackById(@PathVariable int id) {
        Track track = trackRepo.findById(id);
        if (track == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
        }
        return track;
    }

    @GetMapping("/api/track/all/{id_user}")
    public List<Track> getTracksByUserId(@PathVariable int id_user) {
        List<Track> tracks = trackRepo.findByUserId(id_user); // Méthode du repository pour récupérer tous les tracks
        if (tracks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tracks found for the given user");
        }
        return tracks;
    }

    @GetMapping("/api/track/limit/{limit}")
    public List<Track> getRecentTracks(@PathVariable int limit) {
        return trackRepo.findLast(limit);
    }

    @GetMapping("/api/track/genre/{genre}")
    public List<Track> getTracksByGenre(@PathVariable String genre) {
        return trackRepo.findByGenre(genre);
    }

    @DeleteMapping("/api/track/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrack(@PathVariable int id) {
        if (!trackRepo.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
        }
    }

    @PutMapping(value = "/api/track/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Track> updateTrack(
            @PathVariable int id,
            @RequestPart("track") String trackJson,
            @RequestPart(value = "cover", required = false) MultipartFile cover,
            @RequestPart(value = "audio", required = false) MultipartFile audio) {

        // Vérifier si le track existe
        Track existingTrack = trackRepo.findById(id);
        if (existingTrack == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
        }

        // Désérialisation du JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Supporte LocalDate
        Track track;
        try {
            track = mapper.readValue(trackJson, Track.class);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid track JSON", e);
        }

        // Mise à jour des fichiers si fournis
        if (cover != null) {
            String renamedCover = UUID.randomUUID() + ".jpg";
            try {
                File coverFile = new File(getUploadFolder("covers"), renamedCover);
                cover.transferTo(coverFile);
                String coverUrl = generateFileUrl("covers", renamedCover);
                track.setCover(coverUrl); // Met à jour l'URL de la cover
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cover upload failed", e);
            }
        }

        if (audio != null) {
            String renamedAudio = UUID.randomUUID() + ".mp3";
            try {
                File audioFile = new File(getUploadFolder("audio"), renamedAudio);
                audio.transferTo(audioFile);
                String audioUrl = generateFileUrl("audio", renamedAudio);
                track.setAudio(audioUrl); // Met à jour l'URL de l'audio
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Audio upload failed", e);
            }
        }

        // Mise à jour des champs du track
        track.setId_track(id);
        trackRepo.update(track);

        return ResponseEntity.ok(track);
    }

    @PostMapping(value = "/api/track", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Track createTrack(
            @RequestPart("track") String trackJson,
            @RequestPart("src") MultipartFile cover,
            @RequestPart("audio") MultipartFile audio) {

        System.out.println("JSON brut reçu : " + trackJson);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Supporte LocalDate
        Track track;

        try {
            track = mapper.readValue(trackJson, Track.class);
            System.out.println("Track désérialisé : " + track);
        } catch (Exception e) {
            System.out.println("Erreur lors de la désérialisation : " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid track JSON", e);
        }

        // Validation de `id_user`
        if (track.getId_user() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id_user est obligatoire.");
        }

        // Traitement des fichiers
        String renamedCover = UUID.randomUUID() + ".jpg";
        String renamedAudio = UUID.randomUUID() + ".mp3";

        try {
            File coverFile = new File(getUploadFolder("covers"), renamedCover);
            File audioFile = new File(getUploadFolder("audio"), renamedAudio);

            System.out.println("Chemin de sauvegarde des fichiers : " + coverFile.getAbsolutePath());
            System.out.println("Chemin de sauvegarde des fichiers : " + audioFile.getAbsolutePath());

            cover.transferTo(coverFile);
            audio.transferTo(audioFile);

            // Génération des URLs publiques
            String coverUrl = generateFileUrl("covers", renamedCover);
            String audioUrl = generateFileUrl("audio", renamedAudio);

            // Associer les URLs publiques au track
            track.setCover(coverUrl);
            track.setAudio(audioUrl);
        } catch (IOException e) {
            System.out.println("Erreur lors du traitement des fichiers : " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed", e);
        }

        // Persistance
        trackRepo.persist(track);
        return track;
    }

    private String generateFileUrl(String folder, String fileName) {
        // URL d'accès public aux fichiers
        return "http://localhost:8080/uploads/" + folder + "/" + fileName;
    }

    private File getUploadFolder(String type) {
        // Chemin absolu vers le répertoire "src/main/resources/static/uploads/{type}"
        String path = System.getProperty("user.dir") + "/src/main/resources/static/uploads/" + type;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

}
