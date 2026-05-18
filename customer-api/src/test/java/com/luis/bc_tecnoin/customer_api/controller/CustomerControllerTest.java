package com.luis.bc_tecnoin.customer_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luis.bc_tecnoin.customer_api.dto.CreateCustomerDTO;
import com.luis.bc_tecnoin.customer_api.dto.CustomerDTO;
import com.luis.bc_tecnoin.customer_api.dto.UpdateCustomerDTO;
import com.luis.bc_tecnoin.customer_api.exception.CustomerExistsException;
import com.luis.bc_tecnoin.customer_api.exception.CustomerNotExistsException;
import com.luis.bc_tecnoin.customer_api.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService service;

    @Test
    void createCustomer_shouldReturn200_whenValid() throws Exception {
        CreateCustomerDTO dto = new CreateCustomerDTO();
        dto.setName("Test");
        dto.setEmail("test@example.com");

        CustomerDTO response = new CustomerDTO(1L, 1L, "Test", "test@example.com", null, null, null, null);

        when(service.createCustomer(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/customers/")
                        .header("X-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void createCustomer_shouldReturn400_whenInvalidBody() throws Exception {
        CreateCustomerDTO dto = new CreateCustomerDTO();

        mockMvc.perform(post("/api/v1/customers/")
                        .header("X-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCustomer_shouldReturn409_whenEmailExists() throws Exception {
        CreateCustomerDTO dto = new CreateCustomerDTO();
        dto.setName("Test");
        dto.setEmail("exists@example.com");

        when(service.createCustomer(any())).thenThrow(new CustomerExistsException("Email already registered"));

        mockMvc.perform(post("/api/v1/customers/")
                        .header("X-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already registered"));
    }

    @Test
    void getCustomer_shouldReturnDTO_whenFound() throws Exception {
        CustomerDTO dto = new CustomerDTO(1L, null, "Test", "test@example.com", null, null, null, null);

        when(service.getCustomer(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void getCustomer_shouldReturn404_whenNotFound() throws Exception {
        when(service.getCustomer(99L)).thenThrow(new CustomerNotExistsException("Customer not found"));

        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listCustomers_shouldReturnPage() throws Exception {
        CustomerDTO dto = new CustomerDTO(1L, null, "Test", null, null, null, null, null);
        Page<CustomerDTO> page = new PageImpl<>(List.of(dto));

        when(service.listCustomers(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/customers/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test"));
    }

    @Test
    void updateCustomer_shouldReturnDTO_whenFound() throws Exception {
        UpdateCustomerDTO dto = new UpdateCustomerDTO();
        dto.setName("Updated");
        dto.setEmail("updated@example.com");

        CustomerDTO response = new CustomerDTO(1L, null, "Updated", "updated@example.com", null, null, null, null);

        when(service.updateCustomer(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void updateCustomer_shouldReturn400_whenInvalidBody() throws Exception {
        UpdateCustomerDTO dto = new UpdateCustomerDTO();

        mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomer_shouldReturn204_whenFound() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCustomer_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new CustomerNotExistsException("Customer not found")).when(service).deleteCustomer(99L);

        mockMvc.perform(delete("/api/v1/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void existsByUserId_shouldReturnTrue() throws Exception {
        when(service.existsByUserId(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/customers/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsByUserId_shouldReturnFalse() throws Exception {
        when(service.existsByUserId(99L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/customers/99/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
