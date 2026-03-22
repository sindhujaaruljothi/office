package com.dundermifflin.api.mcp.service;

import com.dundermifflin.api.mcp.spi.McpPrompt;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class McpPromptRegistry {

    private final Map<String, McpPrompt> promptsByName;

    public McpPromptRegistry(List<McpPrompt> prompts) {
        this.promptsByName = new LinkedHashMap<>();
        for (McpPrompt prompt : prompts) {
            promptsByName.put(prompt.name(), prompt);
        }
    }

    public Collection<McpPrompt> findAll() {
        return promptsByName.values();
    }

    public Optional<McpPrompt> findByName(String name) {
        return Optional.ofNullable(promptsByName.get(name));
    }
}
