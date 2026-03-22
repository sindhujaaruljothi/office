package com.dundermifflin.api.mcp.spi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface McpTool {

    String name();

    String description();

    ObjectNode inputSchema();

    JsonNode call(ObjectNode arguments);
}
