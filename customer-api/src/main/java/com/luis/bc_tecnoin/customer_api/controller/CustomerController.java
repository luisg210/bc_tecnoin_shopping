package com.luis.bc_tecnoin.customer_api.controller;

import com.luis.bc_tecnoin.customer_api.dto.CreateCustomerDTO;
import com.luis.bc_tecnoin.customer_api.dto.CustomerDTO;
import com.luis.bc_tecnoin.customer_api.dto.UpdateCustomerDTO;
import com.luis.bc_tecnoin.customer_api.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Endpoints for customer management")
public class CustomerController {


    private final CustomerService service;

    @PostMapping
    @Operation(summary = "Create customer", description = "Creates a new customer profile")
    @ApiResponse(responseCode = "201", description = "Customer created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestHeader("X-User-Id") String userId,
                                                       @Valid @RequestBody CreateCustomerDTO dto) {
        dto.setUserId(Long.valueOf(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCustomer(dto));
    }

    @GetMapping("{id}")
    @Operation(summary = "Get customer by ID", description = "Returns a customer by their ID")
    @ApiResponse(responseCode = "200", description = "Customer found")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCustomer(id));
    }

    @GetMapping
    @Operation(summary = "List customers", description = "Returns a paginated list of all customers")
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    public ResponseEntity<Page<CustomerDTO>> listCustomers(Pageable pageable) {
        return ResponseEntity.ok(service.listCustomers(pageable));
    }

    @PutMapping("{id}")
    @Operation(summary = "Update customer", description = "Updates an existing customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id,
                                                       @Valid @RequestBody UpdateCustomerDTO dto) {
        return ResponseEntity.ok(service.updateCustomer(id, dto));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete customer", description = "Deletes a customer by ID")
    @ApiResponse(responseCode = "204", description = "Customer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/exists")
    @Operation(summary = "Check customer exists by user ID", description = "Returns whether a customer exists for the given user ID")
    @ApiResponse(responseCode = "200", description = "Exists check performed successfully")
    public ResponseEntity<Boolean> existsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.existsByUserId(userId));
    }

}
