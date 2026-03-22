package com.dundermifflin.api.mcp.service;

import com.dundermifflin.api.mcp.protocol.McpErrorCodes;
import com.dundermifflin.api.mcp.protocol.McpJsonRpcRequest;
import com.dundermifflin.api.mcp.protocol.McpJsonRpcResponse;
import com.dundermifflin.api.mcp.spi.McpPrompt;
import com.dundermifflin.api.mcp.spi.McpPromptArgument;
import com.dundermifflin.api.mcp.spi.McpResource;
import com.dundermifflin.api.mcp.spi.McpTool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class McpRequestHandler {

    private static final String PROTOCOL_VERSION = "2025-11-25";

    private final McpToolRegistry toolRegistry;
    private final McpPromptRegistry promptRegistry;
    private final McpResourceRegistry resourceRegistry;
    private final ObjectMapper objectMapper;

    public McpRequestHandler(
        McpToolRegistry toolRegistry,
        McpPromptRegistry promptRegistry,
        McpResourceRegistry resourceRegistry,
        ObjectMapper objectMapper
    ) {
        this.toolRegistry = toolRegistry;
        this.promptRegistry = promptRegistry;
        this.resourceRegistry = resourceRegistry;
        this.objectMapper = objectMapper;
    }

    public McpJsonRpcResponse handle(McpJsonRpcRequest request) {
        validateRequest(request);

        try {
            Object result = switch (request.method()) {
                case "initialize" -> initializeResult();
                case "ping" -> objectMapper.createObjectNode();
                case "tools/list" -> listToolsResult();
                case "tools/call" -> callToolResult(request.params());
                case "prompts/list" -> listPromptsResult();
                case "prompts/get" -> getPromptResult(request.params());
                case "resources/list" -> listResourcesResult();
                case "resources/read" -> readResourceResult(request.params());
                case "notifications/initialized" -> null;
                default -> throw new McpProtocolException(
                    McpErrorCodes.METHOD_NOT_FOUND,
                    "Method not found: " + request.method()
                );
            };

            if (request.id() == null) {
                return null;
            }
            return McpJsonRpcResponse.success(request.id(), result);
        } catch (McpProtocolException exception) {
            if (request.id() == null) {
                return null;
            }
            return McpJsonRpcResponse.error(request.id(), exception.code(), exception.getMessage());
        } catch (Exception exception) {
            if (request.id() == null) {
                return null;
            }
            return McpJsonRpcResponse.error(
                request.id(),
                McpErrorCodes.INTERNAL_ERROR,
                "Internal server error",
                exception.getMessage()
            );
        }
    }

    private void validateRequest(McpJsonRpcRequest request) {
        if (request == null || request.method() == null || request.method().isBlank()) {
            throw new McpProtocolException(McpErrorCodes.INVALID_REQUEST, "Invalid JSON-RPC request.");
        }
        if (!"2.0".equals(request.jsonrpc())) {
            throw new McpProtocolException(McpErrorCodes.INVALID_REQUEST, "jsonrpc must be '2.0'.");
        }
    }

    private ObjectNode initializeResult() {
        ObjectNode result = objectMapper.createObjectNode();
        result.put("protocolVersion", PROTOCOL_VERSION);

        ObjectNode capabilities = result.putObject("capabilities");
        ObjectNode tools = capabilities.putObject("tools");
        tools.put("listChanged", false);
        ObjectNode prompts = capabilities.putObject("prompts");
        prompts.put("listChanged", false);
        ObjectNode resources = capabilities.putObject("resources");
        resources.put("listChanged", false);
        resources.put("subscribe", false);

        ObjectNode serverInfo = result.putObject("serverInfo");
        serverInfo.put("name", "dunder-mifflin-spring-mcp");
        serverInfo.put("version", "0.0.1");

        result.put(
            "instructions",
            "Use tools for operational data and resources for static handbook context."
        );
        return result;
    }

    private ObjectNode listToolsResult() {
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode toolsArray = result.putArray("tools");

        for (McpTool tool : toolRegistry.findAll()) {
            ObjectNode toolNode = toolsArray.addObject();
            toolNode.put("name", tool.name());
            toolNode.put("description", tool.description());
            toolNode.set("inputSchema", tool.inputSchema());
        }
        return result;
    }

    private JsonNode callToolResult(JsonNode paramsNode) {
        if (paramsNode == null || !paramsNode.hasNonNull("name")) {
            throw new McpProtocolException(
                McpErrorCodes.INVALID_PARAMS,
                "tools/call requires params.name."
            );
        }

        String toolName = paramsNode.get("name").asText();
        ObjectNode arguments = paramsNode.has("arguments") && paramsNode.get("arguments").isObject()
            ? (ObjectNode) paramsNode.get("arguments")
            : objectMapper.createObjectNode();

        McpTool tool = toolRegistry.findByName(toolName)
            .orElseThrow(() -> new McpProtocolException(
                McpErrorCodes.INVALID_PARAMS,
                "Unknown tool: " + toolName
            ));

        return tool.call(arguments);
    }

    private ObjectNode listPromptsResult() {
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode promptsArray = result.putArray("prompts");

        for (McpPrompt prompt : promptRegistry.findAll()) {
            ObjectNode promptNode = promptsArray.addObject();
            promptNode.put("name", prompt.name());
            promptNode.put("title", prompt.title());
            promptNode.put("description", prompt.description());

            ArrayNode arguments = promptNode.putArray("arguments");
            for (McpPromptArgument argument : prompt.arguments()) {
                arguments.addObject()
                    .put("name", argument.name())
                    .put("title", argument.title())
                    .put("description", argument.description())
                    .put("required", argument.required());
            }
        }
        return result;
    }

    private ObjectNode getPromptResult(JsonNode paramsNode) {
        if (paramsNode == null || !paramsNode.hasNonNull("name")) {
            throw new McpProtocolException(
                McpErrorCodes.INVALID_PARAMS,
                "prompts/get requires params.name."
            );
        }

        String promptName = paramsNode.get("name").asText();
        Map<String, String> arguments = new LinkedHashMap<>();
        if (paramsNode.has("arguments") && paramsNode.get("arguments").isObject()) {
            ObjectNode argumentNode = (ObjectNode) paramsNode.get("arguments");
            argumentNode.fields().forEachRemaining(entry -> {
                if (!entry.getValue().isNull()) {
                    arguments.put(entry.getKey(), entry.getValue().asText());
                }
            });
        }

        McpPrompt prompt = promptRegistry.findByName(promptName)
            .orElseThrow(() -> new McpProtocolException(
                McpErrorCodes.INVALID_PARAMS,
                "Unknown prompt: " + promptName
            ));

        return prompt.render(arguments);
    }

    private ObjectNode listResourcesResult() {
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode resourcesArray = result.putArray("resources");

        for (McpResource resource : resourceRegistry.findAll()) {
            ObjectNode resourceNode = resourcesArray.addObject();
            resourceNode.put("uri", resource.uri());
            resourceNode.put("name", resource.name());
            resourceNode.put("description", resource.description());
            resourceNode.put("mimeType", resource.mimeType());
        }
        return result;
    }

    private ObjectNode readResourceResult(JsonNode paramsNode) {
        if (paramsNode == null || !paramsNode.hasNonNull("uri")) {
            throw new McpProtocolException(
                McpErrorCodes.INVALID_PARAMS,
                "resources/read requires params.uri."
            );
        }

        String uri = paramsNode.get("uri").asText();
        McpResource resource = resourceRegistry.findByUri(uri)
            .orElseThrow(() -> new McpProtocolException(
                McpErrorCodes.INVALID_PARAMS,
                "Unknown resource URI: " + uri
            ));

        ObjectNode result = objectMapper.createObjectNode();
        result.putArray("contents").add(resource.read());
        return result;
    }
}
