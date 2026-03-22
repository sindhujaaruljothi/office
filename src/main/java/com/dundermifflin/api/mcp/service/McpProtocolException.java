package com.dundermifflin.api.mcp.service;

public class McpProtocolException extends RuntimeException {

    private final int code;

    public McpProtocolException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int code() {
        return code;
    }
}
