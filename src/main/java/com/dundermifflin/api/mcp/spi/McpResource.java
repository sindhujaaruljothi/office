package com.dundermifflin.api.mcp.spi;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface McpResource {

    String uri();

    String name();

    String description();

    String mimeType();

    ObjectNode read();
}
