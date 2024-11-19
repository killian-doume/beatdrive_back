package com.beatdrive.beatdrive.controller;

import com.beatdrive.beatdrive.entity.Licence_track;
import com.beatdrive.beatdrive.repository.Licence_trackRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class Licence_TrackController {

    @Autowired
    private Licence_trackRepo licenceTrackRepo;

    @GetMapping("/api/licence_track")
    public List<Licence_track> getAllLicences() {
        return licenceTrackRepo.findAll();
    }

    @GetMapping("/api/licence_track/{id}")
    public Licence_track getLicenceById(@PathVariable int id) {
        Licence_track licence = licenceTrackRepo.findById(id);
        if (licence == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Licence not found");
        }
        return licence;
    }

    @PostMapping("/api/licence_track")
    public Licence_track createLicence(@RequestBody Licence_track licenceTrack) {
        if (licenceTrackRepo.persist(licenceTrack)) {
            return licenceTrack;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create licence");
        }
    }

    @PutMapping("/api/licence_track/{id}")
    public ResponseEntity<Licence_track> updateLicence(@PathVariable int id, @RequestBody Licence_track licenceTrack) {
        if (licenceTrackRepo.findById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Licence not found");
        }
        licenceTrack.setId_licence_track(id);
        if (licenceTrackRepo.update(licenceTrack)) {
            return ResponseEntity.ok(licenceTrack);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update licence");
        }
    }

    @DeleteMapping("/api/licence_track/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLicence(@PathVariable int id) {
        if (!licenceTrackRepo.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Licence not found");
        }
    }
}
