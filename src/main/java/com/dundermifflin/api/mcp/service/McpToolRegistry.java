package com.dundermifflin.api.mcp.service;

import com.dundermifflin.api.mcp.spi.McpTool;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class McpToolRegistry {

    private final Map<String, McpTool> toolsByName;

    public McpToolRegistry(List<McpTool> tools) {
        this.toolsByName = new LinkedHashMap<>();
        for (McpTool tool : tools) {
            toolsByName.put(tool.name(), tool);
        }
    }

    public Collection<McpTool> findAll() {
        return toolsByName.values();
    }

    public Optional<McpTool> findByName(String name) {
        return Optional.ofNullable(toolsByName.get(name));
    }
}
