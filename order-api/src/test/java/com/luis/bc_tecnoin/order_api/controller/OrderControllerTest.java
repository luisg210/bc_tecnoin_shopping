package com.luis.bc_tecnoin.order_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luis.bc_tecnoin.order_api.dto.CreateOrderDTO;
import com.luis.bc_tecnoin.order_api.dto.OrderDTO;
import com.luis.bc_tecnoin.order_api.exception.CustomerNotFoundException;
import com.luis.bc_tecnoin.order_api.exception.OrderNotFoundException;
import com.luis.bc_tecnoin.order_api.service.IOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IOrderService service;

    @Test
    void createOrder_shouldReturnDTO() throws Exception {
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setCustomerId(1L);

        OrderDTO response = new OrderDTO();
        response.setId(1L);
        response.setCustomerId(1L);
        response.setStatus("PENDING");

        when(service.createOrder(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/api/v1/orders")
                        .header("X-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createOrder_shouldReturn404_whenCustomerNotFound() throws Exception {
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setCustomerId(99L);

        when(service.createOrder(any(), eq(1L))).thenThrow(new CustomerNotFoundException(99L));

        mockMvc.perform(post("/api/v1/orders")
                        .header("X-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOrderById_shouldReturnDTO_whenFound() throws Exception {
        OrderDTO dto = new OrderDTO();
        dto.setId(1L);
        dto.setStatus("PENDING");

        when(service.getOrderById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getOrderById_shouldReturn404_whenNotFound() throws Exception {
        when(service.getOrderById(99L)).thenThrow(new OrderNotFoundException(99L));

        mockMvc.perform(get("/api/v1/orders/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllOrdersByCustomer_shouldReturnPage() throws Exception {
        OrderDTO dto = new OrderDTO();
        dto.setId(1L);
        dto.setCustomerId(1L);

        Page<OrderDTO> page = new PageImpl<>(List.of(dto));

        when(service.getAllOrders(eq(1L), any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/orders/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    void cancelOrder_shouldReturn204_whenFound() throws Exception {
        mockMvc.perform(patch("/api/v1/orders/1/cancel"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelOrder_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new OrderNotFoundException(99L)).when(service).cancelOrder(99L);

        mockMvc.perform(patch("/api/v1/orders/99/cancel"))
                .andExpect(status().isNotFound());
    }

    @Test
    void finishOrder_shouldReturn204_whenFound() throws Exception {
        mockMvc.perform(patch("/api/v1/orders/1/finish"))
                .andExpect(status().isNoContent());
    }

    @Test
    void finishOrder_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new OrderNotFoundException(99L)).when(service).finishOrder(99L);

        mockMvc.perform(patch("/api/v1/orders/99/finish"))
                .andExpect(status().isNotFound());
    }

    @Test
    void existsById_shouldReturnTrue() throws Exception {
        when(service.existsById(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/orders/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsById_shouldReturnFalse() throws Exception {
        when(service.existsById(99L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/orders/99/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void getOrderStatus_shouldReturnStatus() throws Exception {
        when(service.getOrderStatus(1L)).thenReturn("PENDING");

        mockMvc.perform(get("/api/v1/orders/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("PENDING"));
    }

    @Test
    void getOrderStatus_shouldReturn404_whenNotFound() throws Exception {
        when(service.getOrderStatus(99L)).thenThrow(new OrderNotFoundException(99L));

        mockMvc.perform(get("/api/v1/orders/99/status"))
                .andExpect(status().isNotFound());
    }
}
