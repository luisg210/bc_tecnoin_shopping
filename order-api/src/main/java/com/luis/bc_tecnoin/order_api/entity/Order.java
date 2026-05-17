package com.luis.bc_tecnoin.order_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing an order in the shopping cart system.
 * Each order is linked to a customer and contains metadata about creation.
 */
@Entity
@Table(name = "orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // Foreign key to customer
    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String status; // PENDING, COMPLETED, CANCELLED

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}