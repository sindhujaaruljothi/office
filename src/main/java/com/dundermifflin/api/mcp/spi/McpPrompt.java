package com.dundermifflin.api.mcp.spi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.Map;

public interface McpPrompt {

    String name();

    String title();

    String description();

    List<McpPromptArgument> arguments();

    ObjectNode render(Map<String, String> arguments);
}
