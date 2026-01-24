# Hexagon

Simple CRUD application with a hexagonal architecture.  
- Spring Boot with Flyway, JPA and PostgreSQL.
- Integration test with Testcontainers and flyway migrations.
- Unit test with JUnit and Mockito.

Swagger UI: http://localhost:8080/swagger-ui/index.html

## TODOs

- Implement Product resource with the same architecture

## Flow diagram

`Goal`: Isolate the domain.  
`How`: Define ports and adapters.  
`Benefits`:
- The domain (business logic) uses interfaces (ports) that are easy to mock.
- The infrastructure and application (adapters) implements these interfaces (ports), allowing easy swapping  
(e.g., changing databases or changing web clients without affecting the domain).

### Example for REST API with database
```
Adapter [Controller from APPLICATION] <-> Port [Service from DOMAIN] <-> Adapter [Repository from INFRASTRUCTURE]
```

Detailled flow:

```
# Example for REST API with database

Controller (adapter in from APPLICATION) 
    -> ServicePort (port in from DOMAIN) 
        -> ServiceImpl (service from DOMAIN) 
            -> RepositoryPort (port out from DOMAIN) 
                -> RepositoryAdapter (adapter out from INFRASTRUCTURE) 
                    -> uses JPA Repository (technical detail from INFRASTRUCTURE)
                        -> interacts with Database
```

## Folder structure

This structure takes only the Category resource of the project.  
Hexagonal pattern:
- Domain exposes PORT IN and PORT OUT
- Domain service implements PORT IN and uses PORT OUT
- Application ADAPTER IN uses PORT IN implemented by Domain service
- Infrastructure ADAPTER OUT implements PORT OUT used by Domain service

```
hexagon
│
├── application:    [Handles application-level concerns like DTOs, exception handling, mapping, and validation]
│   └── adapter
│       └── in          CategoryControllerAdapter implements REST endpoints, uses CategoryServicePort
│   └── dto             CategoryRequest, CategoryResponse
│   └── exception       GlogalExceptionHandler
│   └── mapper          CategoryDtoMapper
│   └── validation      ValidationMessage for dto validation
│
├── domain:         [Core business logic and rules]
│   └── model           Category domain model
│   └── port
│       └── in          CategoryServicePort interface
│       └── out         CategoryRepositoryPort interface
│   └── service         CategoryServiceImpl implements CategoryServicePort, uses CategoryRepositoryPort
│
├── infrastructure: [Implementation details for interacting with external systems]
│   └── adapter
│       └── out         CategoryRepositoryAdapter implements CategoryRepositoryPort, uses JPA Repository
│   └── entity          CategoryEntity
│   └── mapper          CategoryEntityMapper
│   └── repository      JPA Repository interfaces
│   └── utils           Utility classes
|
└── common:         [Shared resources across layers]
    └── config          Spring configurations
    └── exception       NotFoundException, ExistException, BadRequestException, etc.
    └── utils           Common utility classes
```

Note: In some projects, the "port in" are called "use cases", and the "port out" are called "gateways".

## Run

Set up a `.env` file with the following variables:

```env
POSTGRES_USER=hexagon_user
POSTGRES_PASSWORD=hexagon_password
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

## Copyright

@ 2026, VA