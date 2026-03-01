package com.pixelpolo.hexagon.utils;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.pixelpolo.hexagon.infrastructure.mongo.entity.CategoryEntityMongo;

public class MongoSeedingUtils {

    // --- CATEGORIES ---

    private static final CategoryEntityMongo MOBILITY = CategoryEntityMongo.builder()
            .categoryId(1L).name("Mobilité").build();

    private static final CategoryEntityMongo LODGEMENT = CategoryEntityMongo.builder()
            .categoryId(2L).name("Logement").build();

    private static final CategoryEntityMongo PROVIDENT_AND_MORTGAGE = CategoryEntityMongo.builder()
            .categoryId(3L).name("Prévoyance et hypothèque").build();

    private static final CategoryEntityMongo ASSISTANCE_AND_TRAVEL = CategoryEntityMongo.builder()
            .categoryId(4L).name("Assistance et Voyage").build();

    private static final CategoryEntityMongo SERVICES = CategoryEntityMongo.builder()
            .categoryId(5L).name("Services").build();

    private static final CategoryEntityMongo PERSONAL_PROTECTION = CategoryEntityMongo.builder()
            .categoryId(6L).name("Protection des personnes").build();

    private static final CategoryEntityMongo PROPERTY_PROTECTION = CategoryEntityMongo.builder()
            .categoryId(7L).name("Protection des biens").build();

    private static final CategoryEntityMongo ACTIVITY_PROTECTION = CategoryEntityMongo.builder()
            .categoryId(8L).name("Protection de l'activité").build();

    private static final CategoryEntityMongo ASSISTANCE_AND_SERVICES = CategoryEntityMongo.builder()
            .categoryId(9L).name("Assistance & services").build();

    // --- SEED ---

    public static void seedCategories(MongoTemplate mongoTemplate) {
        mongoTemplate.save(MOBILITY);
        mongoTemplate.save(LODGEMENT);
        mongoTemplate.save(PROVIDENT_AND_MORTGAGE);
        mongoTemplate.save(ASSISTANCE_AND_TRAVEL);
        mongoTemplate.save(SERVICES);
        mongoTemplate.save(PERSONAL_PROTECTION);
        mongoTemplate.save(PROPERTY_PROTECTION);
        mongoTemplate.save(ACTIVITY_PROTECTION);
        mongoTemplate.save(ASSISTANCE_AND_SERVICES);
    }

}
