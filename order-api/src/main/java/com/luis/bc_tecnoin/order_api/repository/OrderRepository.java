package com.luis.bc_tecnoin.order_api.repository;

import com.luis.bc_tecnoin.order_api.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByCustomerId(Long customerId, Pageable pageable);
}