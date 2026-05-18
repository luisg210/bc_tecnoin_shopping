package com.luis.bc_tecnoin.order_api.service;

import com.luis.bc_tecnoin.order_api.clients.CustomerClient;
import com.luis.bc_tecnoin.order_api.dto.CreateOrderDTO;
import com.luis.bc_tecnoin.order_api.dto.OrderDTO;
import com.luis.bc_tecnoin.order_api.entity.Order;
import com.luis.bc_tecnoin.order_api.exception.CustomerNotFoundException;
import com.luis.bc_tecnoin.order_api.exception.OrderNotFoundException;
import com.luis.bc_tecnoin.order_api.mapper.OrderMapper;
import com.luis.bc_tecnoin.order_api.repository.OrderRepository;
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
class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private OrderMapper mapper;

    @Mock
    private CustomerClient customerClient;

    @InjectMocks
    private OrderServiceImpl service;

    @Test
    void createOrder_shouldSaveAndReturnDTO() {
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setCustomerId(1L);

        Order entity = new Order();
        entity.setCustomerId(1L);
        entity.setStatus("PENDING");

        Order saved = new Order();
        saved.setId(10L);
        saved.setCustomerId(1L);
        saved.setStatus("PENDING");

        OrderDTO expected = new OrderDTO();
        expected.setId(10L);
        expected.setCustomerId(1L);
        expected.setStatus("PENDING");

        when(customerClient.existsByUserId(1L)).thenReturn(true);
        when(repository.save(any(Order.class))).thenReturn(saved);
        when(mapper.toResponseDto(saved)).thenReturn(expected);

        OrderDTO result = service.createOrder(dto, 1L);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getStatus()).isEqualTo("PENDING");
        verify(repository).save(any(Order.class));
    }

    @Test
    void createOrder_shouldThrow_whenCustomerNotFound() {
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setCustomerId(1L);

        when(customerClient.existsByUserId(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.createOrder(dto, 1L))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer with userId 1 not found");

        verify(repository, never()).save(any());
    }

    @Test
    void getOrderById_shouldReturnDTO_whenFound() {
        Order entity = new Order();
        entity.setId(1L);
        entity.setStatus("PENDING");

        OrderDTO expected = new OrderDTO();
        expected.setId(1L);
        expected.setStatus("PENDING");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDto(entity)).thenReturn(expected);

        OrderDTO result = service.getOrderById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo("PENDING");
    }

    @Test
    void getOrderById_shouldThrow_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getOrderById(99L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order with id 99 not found");
    }

    @Test
    void getAllOrders_shouldReturnPage() {
        Order entity = new Order();
        entity.setId(1L);
        entity.setCustomerId(1L);

        OrderDTO dto = new OrderDTO();
        dto.setId(1L);
        dto.setCustomerId(1L);

        Page<Order> page = new PageImpl<>(List.of(entity));

        when(customerClient.existsByUserId(1L)).thenReturn(true);
        when(repository.findAllByCustomerId(1L, PageRequest.of(0, 10))).thenReturn(page);
        when(mapper.toResponseDto(entity)).thenReturn(dto);

        Page<OrderDTO> result = service.getAllOrders(1L, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getAllOrders_shouldThrow_whenCustomerNotFound() {
        when(customerClient.existsByUserId(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.getAllOrders(99L, PageRequest.of(0, 10)))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void cancelOrder_shouldUpdateStatus_whenFound() {
        Order entity = new Order();
        entity.setId(1L);
        entity.setStatus("PENDING");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.cancelOrder(1L);

        assertThat(entity.getStatus()).isEqualTo("CANCELLED");
        verify(repository).save(entity);
    }

    @Test
    void cancelOrder_shouldThrow_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancelOrder(99L))
                .isInstanceOf(OrderNotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void finishOrder_shouldUpdateStatus_whenFound() {
        Order entity = new Order();
        entity.setId(1L);
        entity.setStatus("PENDING");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.finishOrder(1L);

        assertThat(entity.getStatus()).isEqualTo("FINISH");
        verify(repository).save(entity);
    }

    @Test
    void finishOrder_shouldThrow_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.finishOrder(99L))
                .isInstanceOf(OrderNotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void existsById_shouldReturnTrue_whenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        assertThat(service.existsById(1L)).isTrue();
    }

    @Test
    void existsById_shouldReturnFalse_whenNotExists() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThat(service.existsById(99L)).isFalse();
    }

    @Test
    void getOrderStatus_shouldReturnStatus_whenFound() {
        Order entity = new Order();
        entity.setId(1L);
        entity.setStatus("PENDING");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        assertThat(service.getOrderStatus(1L)).isEqualTo("PENDING");
    }

    @Test
    void getOrderStatus_shouldThrow_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getOrderStatus(99L))
                .isInstanceOf(OrderNotFoundException.class);
    }
}
