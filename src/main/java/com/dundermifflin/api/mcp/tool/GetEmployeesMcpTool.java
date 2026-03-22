package com.dundermifflin.api.mcp.tool;

import com.dundermifflin.api.dto.EmployeeDto;
import com.dundermifflin.api.mcp.spi.McpTool;
import com.dundermifflin.api.service.EmployeeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GetEmployeesMcpTool implements McpTool {

    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    public GetEmployeesMcpTool(EmployeeService employeeService, ObjectMapper objectMapper) {
        this.employeeService = employeeService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String name() {
        return "GetEmployees";
    }

    @Override
    public String description() {
        return "Gets a list of Dunder Mifflin employees. Optionally takes a positive limit.";
    }

    @Override
    public ObjectNode inputSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");

        ObjectNode properties = schema.putObject("properties");
        ObjectNode limit = properties.putObject("limit");
        limit.put("type", "integer");
        limit.put("minimum", 1);
        limit.put("description", "Maximum number of employees to return.");

        schema.put("additionalProperties", false);
        return schema;
    }

    @Override
    public JsonNode call(ObjectNode arguments) {
        int limit = arguments.has("limit") && arguments.get("limit").canConvertToInt()
            ? arguments.get("limit").asInt()
            : 0;

        List<EmployeeDto> employees = employeeService.findAll();
        if (limit > 0 && limit < employees.size()) {
            employees = employees.subList(0, limit);
        }

        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode content = result.putArray("content");
        content.addObject()
            .put("type", "text")
            .put("text", "Returned " + employees.size() + " employees.");

        ObjectNode structuredContent = result.putObject("structuredContent");
        structuredContent.put("count", employees.size());
        structuredContent.set("employees", objectMapper.valueToTree(employees));
        result.put("isError", false);

        return result;
    }
}
