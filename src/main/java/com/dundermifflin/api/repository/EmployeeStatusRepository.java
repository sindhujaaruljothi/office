package com.dundermifflin.api.repository;

import com.dundermifflin.api.domain.entity.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeStatusRepository extends JpaRepository<EmployeeStatus, Integer> {
}
