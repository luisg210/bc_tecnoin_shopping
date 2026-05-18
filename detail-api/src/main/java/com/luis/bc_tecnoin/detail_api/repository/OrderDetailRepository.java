package com.luis.bc_tecnoin.detail_api.repository;

import com.luis.bc_tecnoin.detail_api.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Page<OrderDetail> findAllByOrderId(Long orderId, Pageable pageable);
    boolean existsByOrderId(Long orderId);
}