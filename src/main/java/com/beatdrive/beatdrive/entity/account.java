package com.beatdrive.beatdrive.entity;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class account implements UserDetails {

    private Integer id_account;
    private String nom;
    private String prenom;
    @Email
    @NotBlank
    private String email;
    @JsonProperty(access = Access.WRITE_ONLY)
    @Length(min = 4)
    private String password;
    @NotBlank
    private String pseudo;
    @NotBlank
    private String type;
    private String adresse_facturation;
    private String adresse_livraison;
    private String avatar;
    @NotBlank
    private String telephone;

    public Integer getId_account() {
        return id_account;
    }

    public void setId_account(Integer id_account) {
        this.id_account = id_account;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdresse_facturation() {
        return adresse_facturation;
    }

    public void setAdresse_facturation(String adresse_facturation) {
        this.adresse_facturation = adresse_facturation;
    }

    public String getAdresse_livraison() {
        return adresse_livraison;
    }

    public void setAdresse_livraison(String adresse_livraison) {
        this.adresse_livraison = adresse_livraison;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public account(Integer id_account, String nom, String prenom, @Email @NotBlank String email,
            @Length(min = 4) String password, @NotBlank String pseudo, @NotBlank String type,
            String adresse_facturation, String adresse_livraison, String avatar, @NotBlank String telephone) {
        this.id_account = id_account;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
        this.type = type;
        this.adresse_facturation = adresse_facturation;
        this.adresse_livraison = adresse_livraison;
        this.avatar = avatar;
        this.telephone = telephone;
    }

    public account(String nom, String prenom, @Email @NotBlank String email, @Length(min = 4) String password,
            @NotBlank String pseudo, @NotBlank String type, String adresse_facturation, String adresse_livraison,
            String avatar, @NotBlank String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
        this.type = type;
        this.adresse_facturation = adresse_facturation;
        this.adresse_livraison = adresse_livraison;
        this.avatar = avatar;
        this.telephone = telephone;
    }

    public account() {
    }

    @JsonProperty(access = Access.READ_ONLY)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(type));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
