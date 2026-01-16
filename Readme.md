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
### Local Build
```bash
mvn clean install
mvn spring-boot:run
```

### Docker
```bash
docker build -t yow-point .
docker run -p 8080:8080 yow-point
```

## API Documentation
Once running, the Swagger UI is available at: `http://localhost:8080/swagger-ui.html`
