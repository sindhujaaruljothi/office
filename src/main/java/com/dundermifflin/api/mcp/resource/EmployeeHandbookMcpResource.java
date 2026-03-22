package com.dundermifflin.api.mcp.resource;

import com.dundermifflin.api.mcp.spi.McpResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class EmployeeHandbookMcpResource implements McpResource {

    private final ObjectMapper objectMapper;

    public EmployeeHandbookMcpResource(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String uri() {
        return "resource://dundermifflin/employee-handbook";
    }

    @Override
    public String name() {
        return "EmployeeHandbook";
    }

    @Override
    public String description() {
        return "Dunder Mifflin employee handbook overview for policy and conduct context.";
    }

    @Override
    public String mimeType() {
        return "text/markdown";
    }

    @Override
    public ObjectNode read() {
        ObjectNode content = objectMapper.createObjectNode();
        content.put("uri", uri());
        content.put("mimeType", mimeType());
        content.put("text", loadHandbookText());
        return content;
    }

    private String loadHandbookText() {
        try {
            ClassPathResource resource = new ClassPathResource("mcp/employee_handbook.md");
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load MCP resource: employee handbook", exception);
        }
    }
}
