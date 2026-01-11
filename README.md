# Hexagon

Simple CRUD application with a hexagonal architecture.  
- Spring Boot with Flyway and JPA
- PostgreSQL database

## TODOs

- Add Swagger/OpenAPI documentation
- Add tests for mapper
- Add tests for category repository
- Add tests for category controller
- Implement Product resource with the same architecture
- Add CI/CD pipeline

## Flow diagram

`Goal`: Isolate the domain.  
`How`: Define ports and adapters.  
`Benefits`:
- The domain (business logic) uses interfaces (ports) that are easy to mock.
- The infrastructure (adapters) implements these interfaces (ports), allowing easy infrastructure swapping  
(e.g., changing databases or changing web clients without affecting the domain).

```
Adapter [Controller from INFRA] <-> Port [Service from DOMAIN] <-> Adapter [Repository from INFRA]
```

Detailled flow:

```
Controller (adapter in from INFRA) 
    -> ServicePort (port in from DOMAIN) 
        -> ServiceImpl (service from DOMAIN) 
            -> RepositoryPort (port out from DOMAIN) 
                -> RepositoryAdapter (adapter out from INFRA) 
                    -> uses JPA Repository (technical detail from INFRA)
                        -> interacts with Database
```

## Folder structure

This structure takes only the Category resource of the project.

```
hexagon
│
├── application:    [Handles application-level concerns like DTOs, exception handling, mapping, and validation.]
│   └── dto         (CategoryRequest, CategoryResponse)
│   └── exception   (GlogalExceptionHandler, CategoryNotFoundException, etc.)
│   └── mapper      (CategoryMapper)
│   └── validation  (For dto validation messages)
│
├── domain:         [Core business logic and rules.]
│   └── model
│   └── port
│       └── in      (CategoryServicePort interface)
│       └── out     (CategoryRepositoryPort interface)
│   └── service     (CategoryServiceImpl implements CategoryServicePort, uses CategoryRepositoryPort)
│
├── infrastructure: [Implementation details for interacting with external systems.]
│   └── adapter
│       └── in      (CategoryControllerAdapter implements REST endpoints, uses CategoryServicePort)
│       └── out     (CategoryRepositoryAdapter implements CategoryRepositoryPort, uses JPA Repository)
│   └── config      (Spring configurations)
│   └── repository  (JPA Repository interfaces)
│   └── utils       (Utility classes)
```

Note: In some projects, the "port in" are called "use cases", and the "port out" are called "gateways".

## Run

Set up a `.env` file with the following variables:

```env
POSTGRES_USER=hexagon_user
POSTGRES_PASSWORD=hexagon_password
```

```bash
# Run the application with Docker Compose and rebuild images
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