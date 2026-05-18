package com.luis.bc_tecnoin.payment_api.repository;

import com.luis.bc_tecnoin.payment_api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrderId(Long orderId);
}