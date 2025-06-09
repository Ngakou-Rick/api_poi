package com.poi.yow_point.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Important

import com.poi.yow_point.models.User;
import com.poi.yow_point.repositories.UserRepository;

import java.util.Optional;
// import java.util.UUID;

// La création d'utilisateur est généralement gérée par Keycloak.
// Ce service pourrait être utilisé pour synchroniser/créer une représentation locale après création dans Keycloak,
// ou pour récupérer des informations utilisateur.

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<User> findByUserId(String userId) {
        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Méthode pour créer ou mettre à jour un utilisateur localement (par exemple, après une connexion via Keycloak)
    @Transactional
    public User createOrUpdateUser(User user) {
        // Logique pour vérifier si l'utilisateur existe déjà par email ou username avant de sauvegarder
        return userRepository.save(user);
    }

    // Pas de méthode de suppression typique ici, car la suppression est gérée par Keycloak.
    // Vous pourriez avoir une méthode pour désactiver un utilisateur localement ou supprimer ses données liées.
}