package com.dundermifflin.api.dto;

public record EmployeeDto(
    Integer employeeId,
    String firstName,
    String lastName,
    String title,
    String statusCode
) {
}
