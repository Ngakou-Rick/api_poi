# Guide d'Intégration Liquibase + Spring WebFlux (R2DBC)

Ce document explique comment Liquibase est intégré dans ce projet réactif et comment le maintenir correctement.

## Pourquoi JDBC pour Liquibase ?

Bien que l'application utilise **R2DBC** pour l'accès aux données de manière asynchrone et non-bloquante, Liquibase ne supporte pas nativement R2DBC. Pour les migrations de base de données, un accès séquentiel et transactionnel classique est préférable. Nous utilisons donc le driver **JDBC PostgreSQL** exclusivement pour les migrations au démarrage de l'application.

## 1. Dépendances Maven

Les dépendances suivantes ont été ajoutées dans le `pom.xml` :
- `liquibase-core` : Le moteur de migration.
- `spring-boot-starter-jdbc` : Nécessaire pour que Spring Boot puisse créer une `DataSource` JDBC pour Liquibase.
- `org.postgresql:postgresql` : Driver JDBC PostgreSQL (en scope `runtime`).
- `com.h2database:h2` : Driver JDBC H2 (en scope `test`) pour permettre l'exécution des migrations pendant les tests unitaires/intégration.

## 2. Configuration (`application.properties`)

Pour éviter que Spring Boot ne tente de créer une `DataSource` globale utilisée par Spring Data, nous configurons Liquibase avec ses propres propriétés de connexion :

```properties
# Configuration Liquibase
spring.liquibase.url=jdbc:postgresql://localhost:5432/yow_poi
spring.liquibase.user=postgres
spring.liquibase.password=postgres
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml
spring.liquibase.enabled=true

# Désactivation de l'initialisation standard pour éviter les conflits
spring.sql.init.mode=never
spring.r2dbc.initialization-mode=never
```

### Configuration des Tests

Dans `src/test/resources/application-test.properties`, Liquibase est configuré pour utiliser la base H2 en mémoire :

```properties
spring.liquibase.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.liquibase.user=sa
spring.liquibase.password=
```

## 3. Structure du Changelog

Le fichier maître est situé dans `src/main/resources/db/changelog/db.changelog-master.yaml`.

### Bonnes pratiques utilisées :
- **Utilisation de propriétés** : Pour assurer la compatibilité entre PostgreSQL (prod/dev) et H2 (tests), nous utilisons des propriétés `${uuid.function}`, `${now.function}`, etc.
- **UUID et Timestamps** : Les types UUID et les zones de temps sont gérés de manière native.
- **Contraintes** : Les clés primaires, étrangères et contraintes uniques sont définies en YAML.
- **Indices Spéciaux** : Pour les types PostGIS (`GEOGRAPHY`), nous utilisons des blocs `<sql>` avec l'attribut `dbms="postgresql"`.

## 4. Erreurs classiques à éviter

1. **Conflit d'initialisation** : Si `spring.sql.init.mode` n'est pas à `never`, Spring Boot peut essayer d'exécuter `schema.sql` en plus de Liquibase, provoquant des erreurs de "table already exists".
2. **Types CLOB sur H2** : H2 ne supporte pas les index sur les colonnes de type `TEXT` (mappées en `CLOB`). Pour les colonnes indexées, nous utilisons un type `VARCHAR(255)` sur H2 via la propriété `${indexed_text.type}`.
3. **AddCheckConstraint** : Cette balise YAML Liquibase fait partie de l'offre commerciale. Utilisez une balise `<sql>` pour les contraintes `CHECK`.
4. **Oubli du driver JDBC** : En WebFlux, on a tendance à n'inclure que `r2dbc-postgresql`. Liquibase échouera sans le driver JDBC classique.

## 5. Gestion des profils

- **Dev** : Utilise les valeurs par défaut dans `application.properties`.
- **Test** : Utilise H2 (configuré dans `application-test.properties`).
- **Prod** : Les variables d'environnement (`SPRING_LIQUIBASE_URL`, etc.) doivent surcharger les valeurs de base.
