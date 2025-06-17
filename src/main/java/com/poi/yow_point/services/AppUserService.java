package com.poi.yow_point.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poi.yow_point.models.AppUser;
import com.poi.yow_point.repositories.AppUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) { 
        this.appUserRepository = appUserRepository;
        
    }

    public AppUser saveUser(AppUser appUser) {
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
