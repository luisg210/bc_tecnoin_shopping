package com.luis.bc_tecnoin.customer_api.service;

import com.luis.bc_tecnoin.customer_api.clients.AuthClient;
import com.luis.bc_tecnoin.customer_api.dto.CreateCustomerDTO;
import com.luis.bc_tecnoin.customer_api.dto.CustomerDTO;
import com.luis.bc_tecnoin.customer_api.dto.UpdateCustomerDTO;
import com.luis.bc_tecnoin.customer_api.entity.Customer;
import com.luis.bc_tecnoin.customer_api.exception.CustomerExistsException;
import com.luis.bc_tecnoin.customer_api.exception.CustomerNotExistsException;
import com.luis.bc_tecnoin.customer_api.mapper.CustomerMapper;
import com.luis.bc_tecnoin.customer_api.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomerService implements ICustomerService {


    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final AuthClient authClient;

    @Override
    public CustomerDTO createCustomer(CreateCustomerDTO dto) {
        log.info("Creating customer with email: {}", dto.getEmail());

        // Validate user exists in Auth Service
        if (!authClient.existsById(dto.getUserId())) {
            log.info("User doesn't exists: {}", dto.getUserId());
            throw new CustomerNotExistsException("User does not exist");
        }

        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            log.warn("Duplicate email detected: {}", dto.getEmail());
            throw new CustomerExistsException("Email already registered");
        }

        Customer customer = mapper.toEntity(dto);
        Customer saved = repository.save(customer);

        log.debug("Customer persisted with ID: {}", saved.getCustomerId());
        return mapper.toDTO(saved);
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        log.info("Fetching customer with ID: {}", id);

        Customer customer = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new CustomerNotExistsException("Customer not found");
                });

        return mapper.toDTO(customer);
    }

    @Override
    public Page<CustomerDTO> listCustomers(Pageable pageable) {
        log.info("Listing customers with pageable: {}", pageable);
        return repository.findAll(pageable).map(mapper::toDTO);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, UpdateCustomerDTO dto) {
        log.info("Updating customer with ID: {}", id);

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotExistsException("Customer not found"));

        mapper.updateEntityFromDTO(dto, customer);
        Customer updated = repository.save(customer);

        log.debug("Customer updated with ID: {}", updated.getCustomerId());
        return mapper.toDTO(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);

        if (!repository.existsById(id)) {
            log.error("Customer not found for deletion: {}", id);
            throw new CustomerNotExistsException("Customer not found");
        }

        repository.deleteById(id);
        log.debug("Customer deleted with ID: {}", id);
    }
}