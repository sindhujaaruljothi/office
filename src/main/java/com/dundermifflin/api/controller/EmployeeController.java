package com.dundermifflin.api.controller;

import com.dundermifflin.api.common.controller.AbstractReadOnlyController;
import com.dundermifflin.api.dto.EmployeeDto;
import com.dundermifflin.api.service.EmployeeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController extends AbstractReadOnlyController<EmployeeDto, Integer> {

    public EmployeeController(EmployeeService employeeService) {
        super(employeeService);
    }
}
