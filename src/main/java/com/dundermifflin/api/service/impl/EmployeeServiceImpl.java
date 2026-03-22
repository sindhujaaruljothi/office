package com.dundermifflin.api.service.impl;

import com.dundermifflin.api.dto.EmployeeDto;
import com.dundermifflin.api.exception.ResourceNotFoundException;
import com.dundermifflin.api.mapper.EmployeeMapper;
import com.dundermifflin.api.repository.EmployeeRepository;
import com.dundermifflin.api.service.EmployeeService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll()
            .stream()
            .map(employeeMapper::toDto)
            .toList();
    }

    @Override
    public EmployeeDto findById(Integer id) {
        return employeeRepository.findById(id)
            .map(employeeMapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for id: " + id));
    }
}
