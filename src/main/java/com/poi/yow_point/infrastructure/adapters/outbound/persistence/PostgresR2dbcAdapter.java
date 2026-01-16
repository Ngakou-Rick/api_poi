package com.poi.yow_point.infrastructure.adapters.outbound.persistence;

import com.poi.yow_point.domain.model.AppUser;
import com.poi.yow_point.domain.ports.out.AppUserRepositoryPort;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.mappers.PersistenceMapper;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostgresR2dbcAdapter implements AppUserRepositoryPort {

    private final AppUserRepository appUserRepository;
    private final PersistenceMapper mapper;

    @Override
    public Mono<AppUser> save(AppUser appUser) {
        return appUserRepository.save(mapper.toEntity(appUser))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<AppUser> findById(UUID id) {
        return appUserRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<AppUser> findAll() {
        return appUserRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return appUserRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return appUserRepository.existsById(id);
    }

    @Override
    public Flux<AppUser> findByOrgIdAndIsActive(UUID orgId, Boolean isActive) {
        return appUserRepository.findByOrgIdAndIsActive(orgId, isActive)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<AppUser> findByRole(String role) {
        return appUserRepository.findByRole(role)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Long> countActiveUsersByOrgId(UUID orgId) {
        return appUserRepository.countActiveUsersByOrgId(orgId);
    }
}
