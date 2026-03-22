package com.dundermifflin.api.mcp.spi;

public record McpPromptArgument(
    String name,
    String title,
    String description,
    boolean required
) {
}
