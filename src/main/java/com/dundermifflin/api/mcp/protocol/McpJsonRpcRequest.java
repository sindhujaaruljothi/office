package com.dundermifflin.api.mcp.protocol;

import com.fasterxml.jackson.databind.JsonNode;

public record McpJsonRpcRequest(
    String jsonrpc,
    Object id,
    String method,
    JsonNode params
) {
}
