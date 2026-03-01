package com.pixelpolo.hexagon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.pixelpolo.hexagon.infrastructure.mongo")
@EnableJpaRepositories(basePackages = "com.pixelpolo.hexagon.infrastructure.postgres")
public class HexagonApplication {

    public static void main(String[] args) {
        SpringApplication.run(HexagonApplication.class, args);
    }

}
