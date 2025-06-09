package com.poi.yow_point.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.oauth2.jwt.Jwt; // Si vous utilisez JWT directement
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken; // Autre option
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poi.yow_point.models.User;
import com.poi.yow_point.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Endpoint pour que l'utilisateur authentifié récupère son propre profil
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUserProfile(Principal principal) { // Ou @AuthenticationPrincipal Jwt jwt
        if (principal == null) {
            return ResponseEntity.status(401).build(); // Non autorisé
        }
        // Le nom du principal est souvent l'ID utilisateur de Keycloak (le 'sub') ou le 'preferred_username'
        String userId = principal.getName();

        // Si vous avez besoin de plus d'infos du token JWT (ex: roles, email):
        // if (principal instanceof JwtAuthenticationToken) {
        //     JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) principal;
        //     String emailFromToken = jwtAuthToken.getToken().getClaimAsString("email");
        //     // ... utiliser les infos du token
        // }

        return userService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // Ou créer l'utilisateur localement s'il n'existe pas
    }
}