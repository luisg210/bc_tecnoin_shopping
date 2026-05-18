package com.luis.bc_tecnoin.payment_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing a payment transaction for an order.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String status; // PENDING, SUCCESS, FAILED

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(updatable = false)
    private LocalDateTime updateAt;
}