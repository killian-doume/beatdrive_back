package com.beatdrive.beatdrive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.beatdrive.beatdrive.entity.User;
import com.beatdrive.beatdrive.repository.UserRepo.UserRepository;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserRepository repo;

    @PostMapping("/api/user")
    public User register(@Valid @RequestBody User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        repo.persist(user);
        return user;
    }

    @GetMapping("/api/account")
    public User myAccount(@AuthenticationPrincipal User user) {
        return user;
    }

    @GetMapping("/api/user")
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @PutMapping("/api/user/{id}")
    public User updateUser(@PathVariable int id, @Valid @RequestBody User user) {
        Optional<User> utilisateurOptional = repo.findById(id);
        if (utilisateurOptional.isPresent()) {
            User utilisateur = utilisateurOptional.get();

            utilisateur.setNom(user.getNom());
            utilisateur.setPrenom(user.getPrenom());
            utilisateur.setEmail(user.getEmail());
            utilisateur.setPassword(user.getPassword());
            utilisateur.setPseudo(user.getPseudo());
            utilisateur.setType(user.getType());
            utilisateur.setAdresse_facturation(user.getAdresse_facturation());
            utilisateur.setAdresse_livraison(user.getAdresse_livraison());
            utilisateur.setAvatar(user.getAvatar());
            utilisateur.setTelephone(user.getTelephone());

            if (repo.update(utilisateur)) {
                return utilisateur;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update user");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @DeleteMapping("/api/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        if (!repo.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
