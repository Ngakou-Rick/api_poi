package com.poi.yow_point.domain.ports.out;

import com.poi.yow_point.domain.model.AppUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AppUserRepositoryPort {
    Mono<AppUser> save(AppUser appUser);
    Mono<AppUser> findById(UUID id);
    Mono<AppUser> findByUsername(String username);
    Mono<AppUser> findByEmail(String email);
    Flux<AppUser> findAll();
    Mono<Void> deleteById(UUID id);
    Mono<Boolean> existsById(UUID id);
    Flux<AppUser> findByOrgIdAndIsActive(UUID orgId, Boolean isActive);
    Flux<AppUser> findByRole(String role);
    Mono<Long> countActiveUsersByOrgId(UUID orgId);
}
