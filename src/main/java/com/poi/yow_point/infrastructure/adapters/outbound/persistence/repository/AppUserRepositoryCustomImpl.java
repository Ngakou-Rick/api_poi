package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repositoryUser;

import java.util.UUID;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.AppUserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;

@Repository
class AppUserRepositoryImpl implements AppUserRepositoryCustom {

    private final R2dbcEntityTemplate template;

    public AppUserRepositoryImpl(R2dbcEntityTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<AppUserEntity> findByUsername(String username) {
        return template.selectOne(
                Query.query(Criteria.where("username").is(username)),
                AppUserEntity.class);
    }

    @Override
    public Mono<AppUserEntity> findByEmail(String email) {
        return template.selectOne(
                Query.query(Criteria.where("email").is(email)),
                AppUserEntity.class);
    }

    @Override
    public Mono<Boolean> existsByOrgId(UUID orgId) {
        return template.exists(
                Query.query(Criteria.where("org_id").is(orgId)),
                AppUserEntity.class);
    }

    @Override
    public Mono<Long> countActiveUsersByOrgId(UUID orgId) {
        return template.count(
                Query.query(
                        Criteria.where("org_id").is(orgId)
                                .and("is_active").is(true)),
                AppUserEntity.class);
    }

    @Override
    public Flux<AppUserEntity> findByOrgIdAndIsActive(UUID orgId, Boolean isActive) {
        return template.select(
                Query.query(
                        Criteria.where("org_id").is(orgId)
                                .and("is_active").is(isActive)),
                AppUserEntity.class);
    }

    @Override
    public Flux<AppUserEntity> findByRole(String role) {
        return template.select(
                Query.query(Criteria.where("role").is(role)),
                AppUserEntity.class);
    }

}