package com.beatdrive.beatdrive.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.beatdrive.beatdrive.entity.User;
import com.beatdrive.beatdrive.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
public class AuthController {

    @Autowired
    private UserRepo repo;
    @Autowired
    private PasswordEncoder encoder;

    // Définition d'un endpoint HTTP POST pour l'URL /api/account
    @PostMapping("/api/account")
    public User myAccount(@AuthenticationPrincipal User user) {
        // Retourne directement l'utilisateur authentifié
        return user;
    }

    @GetMapping("/api/user")
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @GetMapping("/api/user/{id}")
    public User getUserById(@PathVariable int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PutMapping(value = "/api/user/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User updateUser(
            @PathVariable int id,
            @RequestPart("user") String userJson,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {

        // Vérifier si l'utilisateur existe
        if (!repo.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        User user;

        try {
            // Désérialiser les données utilisateur
            user = mapper.readValue(userJson, User.class);
            user.setId_user(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user JSON", e);
        }

        // Si un avatar est fourni, traiter le fichier
        if (avatar != null && !avatar.isEmpty()) {
            String renamedAvatar = UUID.randomUUID() + ".jpg";

            try {
                // Enregistrer l'avatar dans le même répertoire que cover
                File avatarFile = new File(getUploadFolder("covers"), renamedAvatar);
                avatar.transferTo(avatarFile);

                // Générer une URL publique pour l'avatar
                String avatarUrl = generateFileUrl("covers", renamedAvatar);

                // Associer l'URL de l'avatar à l'utilisateur
                user.setAvatar(avatarUrl);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed", e);
            }
        }

        // Mettre à jour l'utilisateur dans la base de données
        if (repo.updateuser(user)) {
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update user");
        }
    }

    @PutMapping("/api/user/mdp/{id}")
    public User updatemdp(@PathVariable int id, @Valid @RequestBody User user) {
        if (!repo.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        user.setId_user(id);
        user.setPassword(encoder.encode(user.getPassword()));
        if (repo.updateuser(user)) {
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update user");
        }
    }

    @DeleteMapping("/api/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        if (!repo.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PostMapping(value = "/api/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User register(
            @RequestPart("user") String userJson,
            @RequestPart("cover") MultipartFile cover) {

        System.out.println("JSON brut reçu : " + userJson);

        ObjectMapper mapper = new ObjectMapper();
        User user;

        try {
            user = mapper.readValue(userJson, User.class);
            System.out.println("User désérialisé : " + user);
        } catch (Exception e) {
            System.out.println("Erreur lors de la désérialisation : " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user JSON", e);
        }

        // Vérifie si l'utilisateur existe déjà
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        // Traitement de la cover
        String renamedCover = UUID.randomUUID() + ".jpg";

        try {
            File coverFile = new File(getUploadFolder("covers"), renamedCover);
            System.out.println("Chemin de sauvegarde de la cover : " + coverFile.getAbsolutePath());
            cover.transferTo(coverFile);

            // Génération de l'URL publique
            String coverUrl = generateFileUrl("covers", renamedCover);

            // Associe l'URL au champ `cover` de l'utilisateur
            user.setAvatar(coverUrl);
        } catch (IOException e) {
            System.out.println("Erreur lors du traitement de la cover : " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed", e);
        }

        // Hache le mot de passe avant de persister l'utilisateur
        user.setPassword(encoder.encode(user.getPassword()));

        // Persistance
        repo.persist(user);

        return user;
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
