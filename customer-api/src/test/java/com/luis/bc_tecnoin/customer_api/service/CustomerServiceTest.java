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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerMapper mapper;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private CustomerService service;

    @Test
    void createCustomer_shouldSaveAndReturnDTO() {
        CreateCustomerDTO dto = new CreateCustomerDTO();
        dto.setUserId(1L);
        dto.setEmail("test@example.com");
        dto.setName("Test User");

        Customer entity = new Customer();
        entity.setCustomerId(1L);
        entity.setUserId(1L);
        entity.setEmail("test@example.com");
        entity.setName("Test User");

        Customer saved = new Customer();
        saved.setCustomerId(1L);
        saved.setUserId(1L);
        saved.setEmail("test@example.com");
        saved.setName("Test User");

        CustomerDTO expected = new CustomerDTO(1L, 1L, "Test User", "test@example.com", null, null, null, null);

        when(authClient.existsById(1L)).thenReturn(true);
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(expected);

        CustomerDTO result = service.createCustomer(dto);

        assertThat(result).isEqualTo(expected);
        verify(repository).save(entity);
    }

    @Test
    void createCustomer_shouldThrow_whenUserNotFound() {
        CreateCustomerDTO dto = new CreateCustomerDTO();
        dto.setUserId(99L);

        when(authClient.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.createCustomer(dto))
                .isInstanceOf(CustomerNotExistsException.class)
                .hasMessageContaining("User does not exist");

        verify(repository, never()).save(any());
    }

    @Test
    void createCustomer_shouldThrow_whenEmailExists() {
        CreateCustomerDTO dto = new CreateCustomerDTO();
        dto.setUserId(1L);
        dto.setEmail("existing@example.com");

        when(authClient.existsById(1L)).thenReturn(true);
        when(repository.findByEmail("existing@example.com")).thenReturn(Optional.of(new Customer()));

        assertThatThrownBy(() -> service.createCustomer(dto))
                .isInstanceOf(CustomerExistsException.class)
                .hasMessageContaining("Email already registered");

        verify(repository, never()).save(any());
    }

    @Test
    void getCustomer_shouldReturnDTO_whenFound() {
        Customer entity = new Customer();
        entity.setCustomerId(1L);
        entity.setName("Test User");

        CustomerDTO expected = new CustomerDTO(1L, null, "Test User", null, null, null, null, null);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(expected);

        CustomerDTO result = service.getCustomer(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getCustomer_shouldThrow_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCustomer(99L))
                .isInstanceOf(CustomerNotExistsException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    void listCustomers_shouldReturnPage() {
        Customer entity = new Customer();
        CustomerDTO dto = new CustomerDTO(1L, null, "Test", null, null, null, null, null);
        Page<Customer> page = new PageImpl<>(List.of(entity));

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDTO(entity)).thenReturn(dto);

        Page<CustomerDTO> result = service.listCustomers(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(dto);
    }

    @Test
    void updateCustomer_shouldUpdateAndReturnDTO_whenFound() {
        UpdateCustomerDTO updateDTO = new UpdateCustomerDTO();
        updateDTO.setName("Updated");
        updateDTO.setEmail("updated@example.com");

        Customer existing = new Customer();
        existing.setCustomerId(1L);
        existing.setName("Old");
        existing.setEmail("old@example.com");

        Customer updated = new Customer();
        updated.setCustomerId(1L);
        updated.setName("Updated");
        updated.setEmail("updated@example.com");

        CustomerDTO expected = new CustomerDTO(1L, null, "Updated", "updated@example.com", null, null, null, null);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(expected);

        CustomerDTO result = service.updateCustomer(1L, updateDTO);

        assertThat(result).isEqualTo(expected);
        verify(mapper).updateEntityFromDTO(updateDTO, existing);
    }

    @Test
    void updateCustomer_shouldThrow_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCustomer(99L, new UpdateCustomerDTO()))
                .isInstanceOf(CustomerNotExistsException.class);
    }

    @Test
    void deleteCustomer_shouldDelete_whenFound() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteCustomer(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteCustomer_shouldThrow_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteCustomer(99L))
                .isInstanceOf(CustomerNotExistsException.class);

        verify(repository, never()).deleteById(any());
    }

    @Test
    void existsByUserId_shouldReturnTrue_whenExists() {
        when(repository.existsByUserId(1L)).thenReturn(true);

        assertThat(service.existsByUserId(1L)).isTrue();
    }

    @Test
    void existsByUserId_shouldReturnFalse_whenNotExists() {
        when(repository.existsByUserId(99L)).thenReturn(false);

        assertThat(service.existsByUserId(99L)).isFalse();
    }
}
