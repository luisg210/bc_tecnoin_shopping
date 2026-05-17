package com.luis.bc_tecnoin.customer_api.repository;

import com.luis.bc_tecnoin.customer_api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    boolean existsByUserId(Long id);
}