package com.yowyob.yowyob_point_of_interest_api.service;

import com.yowyob.yowyob_point_of_interest_api.model.AppUser;
import com.yowyob.yowyob_point_of_interest_api.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import org.springframework.security.crypto.password.PasswordEncoder; // If using Spring Security

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    // private final PasswordEncoder passwordEncoder; // If using Spring Security

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) { // , PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        // this.passwordEncoder = passwordEncoder;
    }

    public AppUser saveUser(AppUser appUser) {
        // TODO: Add password encoding if Spring Security is used
        // appUser.setPasswordHash(passwordEncoder.encode(appUser.getPasswordHash()));
        return appUserRepository.save(appUser);
    }

    public Optional<AppUser> getUserById(UUID id) {
        return appUserRepository.findById(id);
    }

    public Optional<AppUser> getUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public Optional<AppUser> getUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public void deleteUser(UUID id) {
        appUserRepository.deleteById(id);
    }

    // Add other business logic methods as needed
}
