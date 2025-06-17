package com.poi.yow_point.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poi.yow_point.models.AppUser;
import com.poi.yow_point.services.AppUserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users") // Base path for user-related APIs
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
        // Consider a DTO for user creation to avoid exposing password hash directly if not handled by frontend
        AppUser savedUser = appUserService.saveUser(appUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable UUID id) {
        return appUserService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable String username) {
        return appUserService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<AppUser> getUserByEmail(@PathVariable String email) {
        // Ensure email is properly encoded if it can contain special characters in URL
        return appUserService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = appUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable UUID id, @RequestBody AppUser userDetails) {
         AppUser user = appUserService.getUserById(id)
            .orElse(null); // Or throw exception

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        // Update relevant fields, avoid updating password hash directly here unless it's a specific password change endpoint
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setRole(userDetails.getRole());
        user.setActive(userDetails.isActive());
        // user.setOrganization(userDetails.getOrganization()); // Org change might have implications

        AppUser updatedUser = appUserService.saveUser(user); // saveUser might re-encode password if not careful
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
