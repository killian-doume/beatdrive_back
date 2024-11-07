package com.beatdrive.beatdrive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.beatdrive.beatdrive.entity.User;
import com.beatdrive.beatdrive.repository.UserRepo.UserRepository;

import jakarta.validation.Valid;
import java.util.List;

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

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return repo.findAll();
    }
}
