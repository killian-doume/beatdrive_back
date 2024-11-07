package com.beatdrive.beatdrive.controller;

import com.beatdrive.beatdrive.entity.Track;
import com.beatdrive.beatdrive.repository.TrackRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

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

    @GetMapping("/api/track/{limit}")
    public List<Track> getRecentTracks(@PathVariable int limit) {
        return trackRepo.findLast(limit);
    }

    @GetMapping("/api/track/{genre}")
    public List<Track> getTracksByGenre(@PathVariable String genre) {
        return trackRepo.findByGenre(genre);
    }

    @PostMapping("/api/track")
    public Track createTrack(@RequestBody Track track) {
        if (trackRepo.persist(track)) {
            return track;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create track");
        }
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
