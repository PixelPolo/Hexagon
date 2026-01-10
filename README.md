# Hexagon

Simple CRUD application with a hexagonal architecture.  
- Spring boot with Flyway and JPA
- PostgreSQL database

## Run

```bash
# Run the application with Docker Compose and rebuild images
docker compose up -d --build --force-recreate
```

## Stop and clean up[docker-compose.yml](docker-compose.yml)

```bash
# Stop the application, remove containers, networks, images, and volumes
# Prune all unused data (i.e., stopped containers, unused images, unused networks, and build cache)
docker compose down -v --rmi all
docker system prune -a --volumes
```

## Copyright

@ 2026, VA