package com.luis.bc_tecnoin.customer_api.controller;

import com.luis.bc_tecnoin.customer_api.dto.CreateCustomerDTO;
import com.luis.bc_tecnoin.customer_api.dto.CustomerDTO;
import com.luis.bc_tecnoin.customer_api.dto.UpdateCustomerDTO;
import com.luis.bc_tecnoin.customer_api.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/")
@Slf4j
@RequiredArgsConstructor
public class CustomerController {


    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestHeader("X-User-Id") String userId,
                                                      @Valid @RequestBody CreateCustomerDTO dto) {
        dto.setUserId(Long.valueOf(userId));
        return ResponseEntity.ok(service.createCustomer(dto));
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCustomer(id));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> listCustomers(Pageable pageable) {
        return ResponseEntity.ok(service.listCustomers(pageable));
    }

    @PutMapping("{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateCustomerDTO dto) {
        return ResponseEntity.ok(service.updateCustomer(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/customers/{userId}/exists
     * Returns true if a customer profile exists for the given userId.
     */
    @GetMapping("/{userId}/exists")
    public ResponseEntity<Boolean> existsByUserId(@PathVariable Long userId) {
        boolean exists = service.existsByUserId(userId);
        return ResponseEntity.ok(exists);
    }

}