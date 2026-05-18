package com.luis.bc_tecnoin.detail_api.service;

import com.luis.bc_tecnoin.detail_api.dto.CreateOrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.dto.OrderDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderDetailService {
    OrderDetailDTO createOrderDetail(CreateOrderDetailDTO dto);
    OrderDetailDTO getOrderDetailById(Long id);
    Page<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId, Pageable pageable);
    void deleteOrderDetail(Long id);
    boolean existsById(Long id);
    Double calculateTotal(Long orderId);
}