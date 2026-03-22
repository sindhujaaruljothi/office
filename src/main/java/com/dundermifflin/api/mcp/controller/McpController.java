package com.dundermifflin.api.mcp.controller;

import com.dundermifflin.api.mcp.protocol.McpErrorCodes;
import com.dundermifflin.api.mcp.protocol.McpJsonRpcRequest;
import com.dundermifflin.api.mcp.protocol.McpJsonRpcResponse;
import com.dundermifflin.api.mcp.service.McpRequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp")
public class McpController {

    private final McpRequestHandler requestHandler;
    private final ObjectMapper objectMapper;

    public McpController(McpRequestHandler requestHandler, ObjectMapper objectMapper) {
        this.requestHandler = requestHandler;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> handle(@RequestBody JsonNode payload) {
        if (payload == null || payload.isNull()) {
            return ResponseEntity.ok(McpJsonRpcResponse.error(null, McpErrorCodes.INVALID_REQUEST, "Invalid request."));
        }

        if (payload.isArray()) {
            List<McpJsonRpcResponse> responses = new ArrayList<>();
            for (JsonNode node : payload) {
                McpJsonRpcResponse response = handleSingleNode(node);
                if (response != null) {
                    responses.add(response);
                }
            }
            if (responses.isEmpty()) {
                return ResponseEntity.accepted().build();
            }
            return ResponseEntity.ok(responses);
        }

        McpJsonRpcResponse response = handleSingleNode(payload);
        if (response == null) {
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.ok(response);
    }

    private McpJsonRpcResponse handleSingleNode(JsonNode node) {
        try {
            McpJsonRpcRequest request = objectMapper.treeToValue(node, McpJsonRpcRequest.class);
            return requestHandler.handle(request);
        } catch (Exception exception) {
            Object id = node != null && node.has("id") ? objectMapper.convertValue(node.get("id"), Object.class) : null;
            return McpJsonRpcResponse.error(
                id,
                McpErrorCodes.INVALID_REQUEST,
                "Invalid JSON-RPC payload.",
                exception.getMessage()
            );
        }
    }
}
