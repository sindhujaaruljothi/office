package com.dundermifflin.api.repository;

import com.dundermifflin.api.domain.entity.OrderDetail;
import com.dundermifflin.api.domain.entity.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
}
