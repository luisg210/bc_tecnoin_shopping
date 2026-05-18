package com.luis.bc_tecnoin.customer_api.service;

import com.luis.bc_tecnoin.customer_api.dto.CreateCustomerDTO;
import com.luis.bc_tecnoin.customer_api.dto.CustomerDTO;
import com.luis.bc_tecnoin.customer_api.dto.UpdateCustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {
    CustomerDTO createCustomer(CreateCustomerDTO dto);
    CustomerDTO getCustomer(Long id);
    Page<CustomerDTO> listCustomers(Pageable pageable);
    CustomerDTO updateCustomer(Long id, UpdateCustomerDTO dto);
    void deleteCustomer(Long id);
    boolean existsByUserId(Long userId);
}