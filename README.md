# Hexagon

Simple CRUD application with a hexagonal architecture.  
Swagger UI: http://localhost:8080/swagger-ui/index.html

## Profile "postgres"
- Spring Boot with Flyway, JPA and PostgreSQL.
- Integration test with Testcontainers and flyway migrations.
- Unit test with JUnit and Mockito.

## Profile "mongo"
- Spring Boot with MongoDB.
- Integration test with Testcontainers and MongoDB.
- Unit test with JUnit and Mockito.

## Flow example for REST API with database
```
Adapter [Controller from APPLICATION] <-> Port [Service from DOMAIN] <-> Adapter [Repository from INFRASTRUCTURE]
```

### Detailed flow:

```
# Example for REST API with database

--- APPLICATION layer ---

1. Controller (exposes endpoint, delegates to an Adapter)
2. Adapter (uses PORT IN interface exposed by DOMAIN)

--- DOMAIN layer ---

3. Service (implements PORT IN with business logic, uses PORT OUT interface)

--- INFRASTRUCTURE layer ---

4. Adapter (implements PORT OUT, uses the repository for database interaction)
5. Repository (implementation for database interaction)
```

## Folder structure

```
hexagon
│
├── application:        [Entry point for the application, exposing APIs, handling requests]
│   └── adapter             Uses the domain port called UseCase, called by the controller
│   └── controller          REST Controller, delegates to the adapter
│   └── dto                 Request and Response DTOs
│   └── exception           Global handler
│   └── mapper              Between DTOs and domain models
│   └── validation          For request DTOs
│
├── domain:             [Core business logic and rules]
│   └── model               Category domain model
│   └── port
│       └── in              Called UseCase, used by an adapter in the application layer
│       └── out             Called Port, implemented by an adapter in the infrastructure layer
│   └── service             Implements the UseCase to handle business logic, uses the Port for data persistence.
│
├── infrastructure:     [Handles persistence]
│   └── postgres            Provides PostgreSQL implementations for the Port defined in the domain layer
│       └── adapter         Implements the Port of the domain layer, uses the repository for database interaction
│       └── entity          JPA entity
│       └── mapper          Mapper between domain model and entity
│       └── repository      Spring Data JPA repository
|   └── mongo
│       └── ...             Same structure as postgres but with MongoDB implementations
|
└── common:             [Shared resources across layers]
    └── config              Spring configurations
    └── exception           NotFoundException, ExistException, BadRequestException, etc.
    └── utils               Common utility classes
```

## Run

Set the profile in `application.properties`

```properties
#spring.profiles.active=postgres
spring.profiles.active=mongo
```

Set up a `.env` file with the following variables:

```env
POSTGRES_USER=hexagon_user
POSTGRES_PASSWORD=hexagon_password
MONGO_USER=hexagon_user
MONGO_PASSWORD=hexagon_password
```

```bash
# Run the application with Docker Compose, rebuild images and recreate volumes
docker compose up -d --build --force-recreate
```

## Stop and clean up

```bash
# Stop the application, remove containers, networks, images, and volumes
# Prune all unused data (i.e., stopped containers, unused images, unused networks, and build cache)
docker compose down -v --rmi all
docker system prune -a --volumes
```

## TODOs

- Implement Product resource with the same architecture

## Copyright

@ 2026, VA