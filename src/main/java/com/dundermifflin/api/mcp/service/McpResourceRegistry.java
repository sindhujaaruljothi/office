package com.dundermifflin.api.mcp.service;

import com.dundermifflin.api.mcp.spi.McpResource;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class McpResourceRegistry {

    private final Map<String, McpResource> resourcesByUri;

    public McpResourceRegistry(List<McpResource> resources) {
        this.resourcesByUri = new LinkedHashMap<>();
        for (McpResource resource : resources) {
            resourcesByUri.put(resource.uri(), resource);
        }
    }

    public Collection<McpResource> findAll() {
        return resourcesByUri.values();
    }

    public Optional<McpResource> findByUri(String uri) {
        return Optional.ofNullable(resourcesByUri.get(uri));
    }
}
