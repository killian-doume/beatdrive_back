package com.beatdrive.beatdrive.controller;

import com.beatdrive.beatdrive.entity.Detail_commande;
import com.beatdrive.beatdrive.repository.Detail_commandeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class Detail_CommandeController {

    @Autowired
    private Detail_commandeRepo detailCommandeRepo;

    @GetMapping("/api/detail_commande")
    public List<Detail_commande> getAllDetailCommandes() {
        return detailCommandeRepo.findAll();
    }

    @GetMapping("/api/detail_commande/all/{idUser}")
    public List<Detail_commande> getDetailsCommandeByUserId(@PathVariable int idUser) {
        List<Detail_commande> detailsCommande = detailCommandeRepo.findByUserId(idUser);
        if (detailsCommande.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Detail Commande found for the given user ID");
        }
        return detailsCommande;
    }

    @PostMapping("/api/detail_commande")
    public Detail_commande createDetailCommande(@RequestBody Detail_commande detailCommande) {
        if (detailCommandeRepo.persist(detailCommande)) {
            return detailCommande;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create detail commande");
        }
    }

    @PutMapping("/api/detail_commande/{id}")
    public ResponseEntity<Detail_commande> updateDetailCommande(@PathVariable int id,
            @RequestBody Detail_commande detailCommande) {
        if (detailCommandeRepo.findById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail Commande not found");
        }

        detailCommande.setId_detail_commande(id);

        if (detailCommandeRepo.update(detailCommande)) {
            return ResponseEntity.ok(detailCommande);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update detail commande");
        }
    }

    @DeleteMapping("/api/detail_commande/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDetailCommande(@PathVariable int id) {
        if (!detailCommandeRepo.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail Commande not found");
        }
    }
}
