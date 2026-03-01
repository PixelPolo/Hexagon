package com.pixelpolo.hexagon.infrastructure.mongo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity representing a Category in the MongoDB database.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "category")
public class CategoryMongoEntity {

    @Id
    private Long categoryId;

    @Indexed(unique = true)
    private String name;

    private LocalDateTime deletionDate;

}
