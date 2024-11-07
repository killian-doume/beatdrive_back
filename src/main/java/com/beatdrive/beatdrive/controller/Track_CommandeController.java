package com.beatdrive.beatdrive.controller;

import com.beatdrive.beatdrive.entity.Track_commande;
import com.beatdrive.beatdrive.repository.Track_commandeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class Track_CommandeController {

    @Autowired
    private Track_commandeRepo trackCommandeRepo;

    @GetMapping("/api/track_commande")
    public List<Track_commande> getAllTrackCommandes() {
        return trackCommandeRepo.findAll();
    }

    @GetMapping("/api/track_commande/{id}")
    public Track_commande getTrackCommandeById(@PathVariable int id) {
        Track_commande trackCommande = trackCommandeRepo.findById(id);
        if (trackCommande == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track Commande not found");
        }
        return trackCommande;
    }

    @PostMapping("/api/track_commande")
    public Track_commande createTrackCommande(@RequestBody Track_commande trackCommande) {
        if (trackCommandeRepo.persist(trackCommande)) {
            return trackCommande;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create track commande");
        }
    }

    @PutMapping("/api/track_commande/{id}")
    public ResponseEntity<Track_commande> updateTrackCommande(@PathVariable int id,
            @RequestBody Track_commande trackCommande) {
        if (trackCommandeRepo.findById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track Commande not found");
        }
        trackCommande.setId_track_commande(id);
        if (trackCommandeRepo.update(trackCommande)) {
            return ResponseEntity.ok(trackCommande);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update track commande");
        }
    }

    @DeleteMapping("/api/track_commande/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrackCommande(@PathVariable int id) {
        if (!trackCommandeRepo.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track Commande not found");
        }
    }
}
