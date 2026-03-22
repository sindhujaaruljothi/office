package com.dundermifflin.api.mcp.prompt;

import com.dundermifflin.api.mcp.spi.McpPrompt;
import com.dundermifflin.api.mcp.spi.McpPromptArgument;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class EmployeesOverviewPrompt implements McpPrompt {

    private final ObjectMapper objectMapper;

    public EmployeesOverviewPrompt(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String name() {
        return "employees_overview";
    }

    @Override
    public String title() {
        return "Employees Overview";
    }

    @Override
    public String description() {
        return "Template that instructs the model to call GetEmployees and summarize workforce insights.";
    }

    @Override
    public List<McpPromptArgument> arguments() {
        return List.of(
            new McpPromptArgument(
                "limit",
                "Employee Limit",
                "Optional number of employees to request through the GetEmployees tool.",
                false
            ),
            new McpPromptArgument(
                "focus",
                "Analysis Focus",
                "Optional focus area such as managers, sales, or status distribution.",
                false
            )
        );
    }

    @Override
    public ObjectNode render(Map<String, String> arguments) {
        String limit = arguments.getOrDefault("limit", "").trim();
        String focus = arguments.getOrDefault("focus", "").trim();

        String toolInstruction = limit.isEmpty()
            ? "Call MCP tool `GetEmployees` without arguments."
            : "Call MCP tool `GetEmployees` with arguments {\"limit\": " + limit + "}.";

        String focusInstruction = focus.isEmpty()
            ? "Summarize by role and status."
            : "Include a focused section for: " + focus + ".";

        String promptText = """
            You are preparing a Dunder Mifflin workforce overview.
            %s
            %s
            Provide a concise summary and list notable employee records.
            """.formatted(toolInstruction, focusInstruction);

        ObjectNode result = objectMapper.createObjectNode();
        result.put("description", description());
        ArrayNode messages = result.putArray("messages");
        ObjectNode message = messages.addObject();
        message.put("role", "user");
        message.putObject("content")
            .put("type", "text")
            .put("text", promptText);
        return result;
    }
}
