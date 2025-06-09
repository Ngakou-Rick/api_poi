package com.poi.yow_point.models;

import jakarta.persistence.*;
// import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;
// import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // "user" est souvent un mot-clé réservé
public class User {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR) // Pour stocker l'UUID de Keycloak comme String ou convertir en UUID si c'est un UUID natif
    private String userId; // Peut être le 'sub' de Keycloak

    @Column(unique = true)
    private String username;  // Peut être le 'preferred_username' de Keycloak
    
    private String password;

    private String email;    // Peut être l'email de Keycloak

    // Si vous stockez les rôles localement en plus de Keycloak
    // Sinon, les rôles sont principalement gérés par Keycloak et lus depuis le token JWT
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;

    // Autres attributs spécifiques à votre application que Keycloak ne gère pas
    // par exemple, préférences utilisateur, date de dernière connexion gérée par l'app, etc.
}