package com.beatdrive.beatdrive.controller;

import com.beatdrive.beatdrive.entity.Track;
import com.beatdrive.beatdrive.repository.TrackRepo;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    @PostMapping("/api/track")
    public Track createTrack(@RequestPart("track") Track track, @RequestPart("src") MultipartFile cover) {
        String renamedCover = UUID.randomUUID() + ".jpg";
        try {
            Thumbnails.of(cover.getInputStream())
                    .width(900)
                    .toFile(new File(getUploadFolder(), renamedCover));
            Thumbnails.of(cover.getInputStream())
                    .size(200, 200)
                    .crop(Positions.CENTER)
                    .toFile(new File(getUploadFolder(), "thumbnail-" + renamedCover));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur lors du traitement de l'image", e);
        }

        track.setCover(renamedCover);
        trackRepo.persist(track);

        return track;
    }

    private File getUploadFolder() {
        File folder = new File(getClass().getClassLoader().getResource(".").getPath().concat("static/uploads"));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;

    }

    @PutMapping("/api/track/{id}")
    public ResponseEntity<Track> updateTrack(@PathVariable int id, @RequestBody Track track) {
        if (trackRepo.findById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
        }
        track.setId_track(id);
        if (trackRepo.update(track)) {
            return ResponseEntity.ok(track);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update track");
        }
    }

    @DeleteMapping("/api/track/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrack(@PathVariable int id) {
        if (!trackRepo.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
        }
    }
}
