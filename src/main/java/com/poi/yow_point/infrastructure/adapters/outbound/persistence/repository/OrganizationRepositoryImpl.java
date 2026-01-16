package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.OrganizationEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OrganizationRepositoryImpl implements OrganizationRepositoryCustom {

    private final R2dbcEntityTemplate template;

    public OrganizationRepositoryImpl(R2dbcEntityTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<OrganizationEntity> findByOrgCode(String orgCode) {
        return template.select(OrganizationEntity.class)
                .matching(Query.query(Criteria.where("org_code").is(orgCode)))
                .one();
    }

    @Override
    public Flux<OrganizationEntity> findAllActive() {
        return template.select(OrganizationEntity.class)
                .matching(Query.query(Criteria.where("is_active").is(true)))
                .all();
    }

    // Recherche par type d'organisation avec criteria
    public Flux<OrganizationEntity> findByOrgType(String orgType) {
        return template.select(OrganizationEntity.class)
                .matching(Query.query(Criteria.where("org_type").is(orgType)))
                .all();
    }

    // Recherche par statut actif avec criteria
    public Flux<OrganizationEntity> findByIsActive(Boolean isActive) {
        return template.select(OrganizationEntity.class)
                .matching(Query.query(Criteria.where("is_active").is(isActive)))
                .all();
    }

    // Recherche par nom contenant (case insensitive) avec criteria
    public Flux<OrganizationEntity> findByOrgName(String orgName) {
        return template.select(OrganizationEntity.class)
                .matching(Query.query(Criteria.where("org_name").like("%" + orgName + "%").ignoreCase(true)))
                .all();
    }

    // Recherche combinée par type et statut avec criteria
    public Flux<OrganizationEntity> findByOrgTypeAndIsActive(String orgType, Boolean isActive) {
        Criteria criteria = Criteria.where("org_type").is(orgType)
                .and("is_active").is(isActive);

        return template.select(OrganizationEntity.class)
                .matching(Query.query(criteria))
                .all();
    }
}
