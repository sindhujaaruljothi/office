package com.dundermifflin.api.repository;

import com.dundermifflin.api.domain.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Integer> {
}
