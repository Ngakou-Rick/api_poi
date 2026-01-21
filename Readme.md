# YowPoint Reactive API

This project is a reactive microservice for managing Points of Interest, built with Spring Boot 3.2.6 and following Hexagonal Architecture.

## Architecture
- **Domain**: Pure business logic and models.
- **Application**: Use cases and service orchestration.
- **Infrastructure**: Technical adapters (REST, Persistence, Kafka, Redis).

## Tech Stack
- Java 21
- Spring WebFlux
- Spring Data R2DBC (PostgreSQL)
- Spring Data Redis (Reactive)
- Reactor Kafka
- Resilience4j
- MapStruct & Lombok

## Getting Started

### 1. Database and Environment (Docker)
This project requires PostgreSQL (with PostGIS), Redis, Kafka, and Elasticsearch.
The easiest way to start them is via Docker Compose:

```bash
docker-compose up -d
```

**Note sur le port PostgreSQL :** Le projet est configuré pour se connecter sur le port **5433** (configuré dans `docker-compose.yml` pour éviter les conflits avec une instance locale).

### 2. Backend (Spring Boot)
Une fois les conteneurs démarrés :
```bash
./mvnw clean spring-boot:run
```
Le backend tourne sur `http://localhost:8080`.

### 3. Frontend (Next.js)
Dans un autre terminal :
```bash
cd frontend
npm install
npm run dev
```
Le frontend tourne sur `http://localhost:3000`.

### Docker (App complète)
```bash
docker build -t yow-point .
docker run -p 8080:8080 yow-point
```

## API Documentation
Once running, the Swagger UI is available at: `http://localhost:8080/swagger-ui.html`
