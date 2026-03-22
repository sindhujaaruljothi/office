package com.dundermifflin.api.mcp.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record McpJsonRpcError(
    int code,
    String message,
    Object data
) {
}
