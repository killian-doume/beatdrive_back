package com.beatdrive.beatdrive.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Annotation pour indiquer que cette classe contient des configurations Spring
@Configuration
public class SecurityConfig {

    // Injection de la valeur de la propriété `frontend.url` définie dans les
    // fichiers de configuration
    @Value("${frontend.url}")
    private String frontUrl;

    // Configuration de la chaîne de filtres de sécurité
    @Bean
    SecurityFilterChain authConfig(HttpSecurity http) throws Exception {

        // Configuration de l'authentification HTTP de base (Basic Auth)
        http.httpBasic(Customizer.withDefaults())

                // Configuration de la gestion des sessions
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // Utilise des sessions uniquement si nécessaire

                // Désactivation de la protection CSRF (Cross-Site Request Forgery)
                .csrf(csrf -> csrf.disable())
                // Désactivé, car souvent inutile pour des APIs REST si on utilise des tokens ou
                // Basic Auth

                // Configuration de CORS (Cross-Origin Resource Sharing)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Autorise les requêtes depuis les origines définies dans
                // `corsConfigurationSource()`

                // Configuration de la déconnexion
                .logout(logout -> logout.logoutUrl("/logout"))
                // Définition de l'URL de déconnexion

                // Configuration des autorisations pour les requêtes
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/account").authenticated()
                        // L'accès à l'endpoint `/api/account` nécessite une authentification
                        .anyRequest().permitAll());
        // Toutes les autres requêtes sont autorisées sans authentification

        // Retourne la chaîne de filtres configurée
        return http.build();
    }

    // Bean pour encoder les mots de passe
    @Bean
    PasswordEncoder getEncoder() {
        // Utilise l'algorithme BCrypt avec une force de 12
        return new BCryptPasswordEncoder(12);
    }

    // Configuration de CORS (Cross-Origin Resource Sharing)
    private CorsConfigurationSource corsConfigurationSource() {
        // Crée une configuration pour gérer les requêtes inter-origines
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // Permet l'envoi des cookies ou des informations d'authentification
        configuration.setAllowedOrigins(Arrays.asList(frontUrl)); // Autorise uniquement l'origine spécifiée
                                                                  // dans`frontend.url`
        configuration.setAllowedMethods(Arrays.asList("*")); // Autorise toutes les méthodes HTTP (GET, POST, etc.)
        configuration.setAllowedHeaders(Arrays.asList("*")); // Autorise tous les en-têtes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applique cette configuration à toutes les URL
        return source;
    }
}
