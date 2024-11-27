// Définition d'un package pour organiser le code
package com.beatdrive.beatdrive.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.beatdrive.beatdrive.entity.Track;
import com.beatdrive.beatdrive.repository.TrackRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Annotation pour indiquer que cette classe est un contrôleur REST
@RestController
public class TrackController {

    // Injection automatique du repository pour interagir avec les données de la
    // base
    @Autowired
    private TrackRepo trackRepo;

    // Endpoint pour récupérer tous les tracks
    @GetMapping("/api/track")
    public List<Track> getAllTracks() {
        // Retourne la liste de tous les tracks présents dans la base
        return trackRepo.findAll();
    }

    // Endpoint pour récupérer un track par son ID
    @GetMapping("/api/track/{id}")
    public Track getTrackById(@PathVariable int id) {
        // Recherche le track par son ID
        Track track = trackRepo.findById(id);
        // Si le track n'est pas trouvé, lever une exception HTTP 404
        if (track == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
        }
        // Retourne le track trouvé
        return track;
    }

    // Endpoint pour récupérer tous les tracks d'un utilisateur spécifique
    @GetMapping("/api/track/all/{id_user}")
    public List<Track> getTracksByUserId(@PathVariable int id_user) {
        // Recherche les tracks associés à un utilisateur donné
        List<Track> tracks = trackRepo.findByUserId(id_user);
        // Si aucun track n'est trouvé, lever une exception HTTP 404
        if (tracks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tracks found for the given user");
        }
        // Retourne la liste des tracks
        return tracks;
    }

    // Endpoint pour récupérer les tracks les plus récents avec une limite
    @GetMapping("/api/track/limit/{limit}")
    public List<Track> getRecentTracks(@PathVariable int limit) {
        // Retourne les derniers tracks ajoutés jusqu'à la limite spécifiée
        return trackRepo.findLast(limit);
    }

    // Endpoint pour récupérer les tracks par genre
    @GetMapping("/api/track/genre/{genre}")
    public List<Track> getTracksByGenre(@PathVariable String genre) {
        // Retourne les tracks filtrés par genre
        return trackRepo.findByGenre(genre);
    }

    // Endpoint pour supprimer un track par son ID
    @DeleteMapping("/api/track/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retourne un statut HTTP 204 si la suppression réussit
    public void deleteTrack(@PathVariable int id) {
        // Tente de supprimer le track par ID
        if (!trackRepo.delete(id)) {
            // Si la suppression échoue, lever une exception HTTP 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
        }
    }

    // Endpoint pour mettre à jour un track
    @PutMapping(value = "/api/track/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Track> updateTrack(
            @PathVariable int id,
            @RequestPart("track") String trackJson,
            @RequestPart(value = "cover", required = false) MultipartFile cover,
            @RequestPart(value = "audio", required = false) MultipartFile audio) {

        // Vérifie si le track existe
        Track existingTrack = trackRepo.findById(id);
        if (existingTrack == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
        }

        // Désérialise le JSON en objet Track
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Support des champs de type LocalDate
        Track track;
        try {
            track = mapper.readValue(trackJson, Track.class);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid track JSON", e);
        }

        // Mise à jour de la cover si fournie
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

        // Mise à jour de l'audio si fourni
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

        // Mise à jour du track dans la base
        track.setId_track(id);
        trackRepo.update(track);

        // Retourne le track mis à jour
        return ResponseEntity.ok(track);
    }

    // Endpoint pour créer un nouveau track
    @PostMapping(value = "/api/track", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Track createTrack(
            @RequestPart("track") String trackJson,
            @RequestPart("src") MultipartFile cover,
            @RequestPart("audio") MultipartFile audio) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Support pour les champs LocalDate
        Track track;

        // Désérialise le JSON en objet Track
        try {
            track = mapper.readValue(trackJson, Track.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid track JSON", e);
        }

        // Vérifie si l'utilisateur est spécifié
        if (track.getId_user() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id_user est obligatoire.");
        }

        // Gère le téléchargement des fichiers
        String renamedCover = UUID.randomUUID() + ".jpg";
        String renamedAudio = UUID.randomUUID() + ".mp3";

        try {
            File coverFile = new File(getUploadFolder("covers"), renamedCover);
            File audioFile = new File(getUploadFolder("audio"), renamedAudio);

            cover.transferTo(coverFile);
            audio.transferTo(audioFile);

            String coverUrl = generateFileUrl("covers", renamedCover);
            String audioUrl = generateFileUrl("audio", renamedAudio);

            track.setCover(coverUrl);
            track.setAudio(audioUrl);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed", e);
        }

        // Sauvegarde le nouveau track dans la base
        trackRepo.persist(track);
        return track;
    }

    // Génère une URL publique pour accéder aux fichiers
    private String generateFileUrl(String folder, String fileName) {
        return "http://localhost:8080/uploads/" + folder + "/" + fileName;
    }

    // Retourne le chemin absolu pour les fichiers téléchargés
    private File getUploadFolder(String type) {
        String path = System.getProperty("user.dir") + "/src/main/resources/static/uploads/" + type;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs(); // Crée le dossier s'il n'existe pas
        }
        return folder;
    }
}
