package com.yowyob.yowyob_point_of_interest_api.controller;

import com.yowyob.yowyob_point_of_interest_api.dto.AppUserDTO;
import com.yowyob.yowyob_point_of_interest_api.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux; // Added
import reactor.core.publisher.Mono; // Added

// import java.util.List; // Keep for getAllUsers if service returns List, but should be Flux - Removed
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "APIs for managing application users")
public class AppUserController {

    private static final Logger log = LoggerFactory.getLogger(AppUserController.class);
    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                         content = @Content(schema = @Schema(implementation = AppUserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<ResponseEntity<AppUserDTO>> createUser(@RequestBody AppUserDTO appUserDTO) {
        log.info("Received reactive request to create user: {}", appUserDTO.getUsername());
        return appUserService.saveUser(appUserDTO)
            .map(savedUser -> new ResponseEntity<>(savedUser, HttpStatus.CREATED))
            .doOnSuccess(response -> {
                if (response.getBody() != null) {
                    log.info("User created with ID: {}", response.getBody().getUserId());
                }
            })
            .doOnError(e -> log.error("Error creating user: {}", appUserDTO.getUsername(), e));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                         content = @Content(schema = @Schema(implementation = AppUserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<AppUserDTO>> getUserById(@Parameter(description = "ID of the user") @PathVariable UUID id) {
        log.info("Received reactive request to get user by ID: {}", id);
        return appUserService.getUserById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .doOnError(e -> log.error("Error getting user ID: {}", id, e));
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get a user by their username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                         content = @Content(schema = @Schema(implementation = AppUserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<AppUserDTO>> getUserByUsername(@Parameter(description = "Username") @PathVariable String username) {
        log.info("Received reactive request to get user by username: {}", username);
         return appUserService.getUserByUsername(username)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .doOnError(e -> log.error("Error getting user by username: {}", username, e));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get a user by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                         content = @Content(schema = @Schema(implementation = AppUserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<AppUserDTO>> getUserByEmail(@Parameter(description = "Email") @PathVariable String email) {
        log.info("Received reactive request to get user by email: {}", email);
        return appUserService.getUserByEmail(email)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .doOnError(e -> log.error("Error getting user by email: {}", email, e));
    }

    @GetMapping
    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users",
                 content = @Content(schema = @Schema(implementation = AppUserDTO.class)))
    public Flux<AppUserDTO> getAllUsers() { // Return Flux directly
        log.info("Received reactive request to get all users");
        return appUserService.getAllUsers()
            .doOnComplete(() -> log.info("Finished streaming all users."))
            .doOnError(e -> log.error("Error getting all users", e));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                         content = @Content(schema = @Schema(implementation = AppUserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<ResponseEntity<AppUserDTO>> updateUser(
            @Parameter(description = "ID of the user to be updated") @PathVariable UUID id,
            @RequestBody AppUserDTO userDetailsDTO) {
        log.info("Received reactive request to update user with ID: {}", id);
        return appUserService.updateUser(id, userDetailsDTO)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .doOnSuccess(response -> {
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    log.info("User updated with ID: {}", response.getBody().getUserId());
                } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                    log.warn("User not found for update, ID: {}", id);
                }
            })
            .doOnError(e -> log.error("Error updating user ID: {}", id, e));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<ResponseEntity<Void>> deleteUser(@Parameter(description = "ID of the user to be deleted") @PathVariable UUID id) {
        log.info("Received reactive request to delete user by ID: {}", id);
        return appUserService.deleteUser(id)
            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
            .doOnSuccess(response -> log.info("User deleted with ID: {}", id))
            .doOnError(e -> log.error("Error deleting user ID: {}", id, e));
    }
}
