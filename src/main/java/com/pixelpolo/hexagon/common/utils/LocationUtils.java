package com.pixelpolo.hexagon.common.utils;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for constructing resource location URIs.
 */
@Component
public class LocationUtils {

    @Value("${api.version}")
    private String apiVersion;

    public URI getLocation(Long id, String path) {
        return URI.create("/api/" + apiVersion + "/" + path + "/" + id);
    }

}
