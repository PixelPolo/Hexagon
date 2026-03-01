package com.pixelpolo.hexagon.infrastructure.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import com.pixelpolo.hexagon.infrastructure.mongo.entity.CategoryEntityMongo;
import com.pixelpolo.hexagon.utils.MongoSeedingUtils;

/**
 * Integration tests for CategoryControllerAdapter.
 * It uses Testcontainers to spin up a MongoDB database for testing.
 */
@ActiveProfiles({"test", "mongo"})
public class CategoryControllerAdapterMongoIntegrationTest extends CategoryControllerAbstractIntegrationTest {

    // --- TEST CONTAINERS SETUP ---
    // Docker must be running.
    // Static container shared across all tests.
    // Seeding the database is made after each test with an utility class.

    @Container
    private static final MongoDBContainer MONGO_CONTAINER = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_CONTAINER::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected void resetDatabase() {
        mongoTemplate.dropCollection(CategoryEntityMongo.class);
        MongoSeedingUtils.seedCategories(mongoTemplate);
    }

    // --- TESTS ---

    @Test
    @DisplayName("Testcontainers MongoDB container is running")
    void shouldHaveRunningMongoDBContainer() {
        assertThat(MONGO_CONTAINER.isRunning()).isTrue();
    }

}
