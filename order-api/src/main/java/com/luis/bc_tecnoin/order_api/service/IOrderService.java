package com.luis.bc_tecnoin.order_api.service;

import com.luis.bc_tecnoin.order_api.dto.CreateOrderDTO;
import com.luis.bc_tecnoin.order_api.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    OrderDTO createOrder(CreateOrderDTO dto, Long userId);
    OrderDTO getOrderById(Long id);
    Page<OrderDTO> getAllOrders(Long customerId, Pageable pageable);
    void cancelOrder(Long id);
    void finishOrder(Long id);

    boolean existsById(Long id);
    String getOrderStatus(Long id);
}