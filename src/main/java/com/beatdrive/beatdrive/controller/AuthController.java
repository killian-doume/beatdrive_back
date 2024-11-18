package com.beatdrive.beatdrive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.beatdrive.beatdrive.entity.User;
import com.beatdrive.beatdrive.repository.UserRepo;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
public class AuthController {

    @Autowired
    private UserRepo repo;
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/api/user")
    public User register(@Valid @RequestBody User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        // Hache le mot de passe avant de le persister
        user.setPassword(encoder.encode(user.getPassword()));
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

    @GetMapping("/api/user/{id}")
    public User getUserById(@PathVariable int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PutMapping("/api/user/{id}")
    public User updateUser(@PathVariable int id, @Valid @RequestBody User user) {
        if (!repo.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        user.setId_user(id);
        if (repo.update(user)) {
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
}
