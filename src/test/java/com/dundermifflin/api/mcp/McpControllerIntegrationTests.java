package com.dundermifflin.api.mcp;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class McpControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void initialize_ReturnsServerCapabilities() throws Exception {
        String payload = """
            {
              "jsonrpc": "2.0",
              "id": 1,
              "method": "initialize",
              "params": {
                "protocolVersion": "2025-11-25",
                "capabilities": {}
              }
            }
            """;

        mockMvc.perform(post("/mcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.jsonrpc").value("2.0"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.result.protocolVersion").value("2025-11-25"))
            .andExpect(jsonPath("$.result.serverInfo.name").value("dunder-mifflin-spring-mcp"));
    }

    @Test
    void toolsList_ReturnsGetEmployeesTool() throws Exception {
        String payload = """
            {
              "jsonrpc": "2.0",
              "id": 2,
              "method": "tools/list"
            }
            """;

        mockMvc.perform(post("/mcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.tools[*].name", hasItem("GetEmployees")));
    }

    @Test
    void toolsCall_ReturnsLimitedEmployees() throws Exception {
        String payload = """
            {
              "jsonrpc": "2.0",
              "id": 3,
              "method": "tools/call",
              "params": {
                "name": "GetEmployees",
                "arguments": {
                  "limit": 1
                }
              }
            }
            """;

        mockMvc.perform(post("/mcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.isError").value(false))
            .andExpect(jsonPath("$.result.structuredContent.count").value(1))
            .andExpect(jsonPath("$.result.structuredContent.employees[0].firstName").value("Michael"));
    }

    @Test
    void resourcesListAndRead_ReturnsEmployeeHandbook() throws Exception {
        String listPayload = """
            {
              "jsonrpc": "2.0",
              "id": 4,
              "method": "resources/list"
            }
            """;

        String readPayload = """
            {
              "jsonrpc": "2.0",
              "id": 5,
              "method": "resources/read",
              "params": {
                "uri": "resource://dundermifflin/employee-handbook"
              }
            }
            """;

        mockMvc.perform(post("/mcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(listPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.resources[*].name", hasItem("EmployeeHandbook")));

        mockMvc.perform(post("/mcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(readPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.contents[0].mimeType").value("text/markdown"))
            .andExpect(jsonPath("$.result.contents[0].text").value(org.hamcrest.Matchers.containsString("Core Principles")));
    }

    @Test
    void promptsListAndGet_ReturnPromptTemplateForToolUse() throws Exception {
        String listPayload = """
            {
              "jsonrpc": "2.0",
              "id": 6,
              "method": "prompts/list"
            }
            """;

        String getPayload = """
            {
              "jsonrpc": "2.0",
              "id": 7,
              "method": "prompts/get",
              "params": {
                "name": "employees_overview",
                "arguments": {
                  "limit": "1",
                  "focus": "managers"
                }
              }
            }
            """;

        mockMvc.perform(post("/mcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(listPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.prompts[*].name", hasItem("employees_overview")));

        mockMvc.perform(post("/mcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.messages[0].role").value("user"))
            .andExpect(jsonPath("$.result.messages[0].content.type").value("text"))
            .andExpect(jsonPath("$.result.messages[0].content.text").value(org.hamcrest.Matchers.containsString("GetEmployees")))
            .andExpect(jsonPath("$.result.messages[0].content.text").value(org.hamcrest.Matchers.containsString("managers")));
    }
}
