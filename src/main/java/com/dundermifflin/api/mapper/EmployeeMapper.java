package com.dundermifflin.api.mapper;

import com.dundermifflin.api.domain.entity.Employee;
import com.dundermifflin.api.dto.EmployeeDto;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeDto toDto(Employee employee) {
        return new EmployeeDto(
            employee.getEmployeeId(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getTitle(),
            employee.getStatusCode()
        );
    }
}
