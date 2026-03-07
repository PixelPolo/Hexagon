package com.pixelpolo.hexagon.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.pixelpolo.hexagon.infrastructure.mongo.entity.CategoryEntityMongo;
import com.pixelpolo.hexagon.common.utils.MongoSeedingUtils;

/**
 * Integration tests for CategoryController.
 * It uses Testcontainers to spin up a MongoDB database for testing.
 */
@ActiveProfiles({"test", "mongo"})
public class CategoryControllerMongoIntegrationTest extends CategoryControllerAbstractIntegrationTest {

    // --- TESTS SETUP ---
    // Use a seeding utility class to populate the MongoDB database.

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected void resetDatabase() {
        mongoTemplate.dropCollection(CategoryEntityMongo.class);
        MongoSeedingUtils.seedCategories(mongoTemplate);
    }

}
