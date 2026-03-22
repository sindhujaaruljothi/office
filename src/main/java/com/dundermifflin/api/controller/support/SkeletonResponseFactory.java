package com.dundermifflin.api.controller.support;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class SkeletonResponseFactory {

    private SkeletonResponseFactory() {
    }

    public static ResponseEntity<Map<String, String>> notImplemented(String resource) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
            .body(Map.of(
                "resource", resource,
                "message", "Skeleton endpoint. Implementation will be added in next step."
            ));
    }
}
