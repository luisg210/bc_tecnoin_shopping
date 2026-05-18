package com.luis.bc_tecnoin.detail_api.service;

import com.luis.bc_tecnoin.detail_api.clients.OrderClient;
import com.luis.bc_tecnoin.detail_api.clients.ProductClient;
import com.luis.bc_tecnoin.detail_api.dto.CreateOrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.dto.OrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.dto.ProductDTO;
import com.luis.bc_tecnoin.detail_api.entity.OrderDetail;
import com.luis.bc_tecnoin.detail_api.exception.InvalidOrderException;
import com.luis.bc_tecnoin.detail_api.exception.OrderDetailNotFoundException;
import com.luis.bc_tecnoin.detail_api.exception.ProductNotFoundException;
import com.luis.bc_tecnoin.detail_api.mapper.OrderDetailMapper;
import com.luis.bc_tecnoin.detail_api.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing order details.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements IOrderDetailService {

    private final OrderDetailRepository repository;
    private final OrderDetailMapper mapper;
    private final OrderClient orderClient;
    private final ProductClient productClient;

    @Override
    public OrderDetailDTO createOrderDetail(CreateOrderDetailDTO dto) {
        log.info("Attempting to create order detail for orderId={}", dto.getOrderId());

        // Validate order existence
        if (!orderClient.existsOrderById(dto.getOrderId())) {
            log.error("Order with id={} not found", dto.getOrderId());
            throw new InvalidOrderException(dto.getOrderId());
        }

        // Fetch product info from Product API
        ProductDTO product = productClient.getById(dto.getProductId());
        if (product == null) {
            log.error("Product with id={} not found", dto.getProductId());
            throw new ProductNotFoundException(dto.getProductId());
        }

        Double unitPrice = product.getPrice();
        log.info("Fetched product price={} for productId={}", unitPrice, dto.getProductId());

        OrderDetail detail = new OrderDetail();
        detail.setOrderId(dto.getOrderId());
        detail.setProductId(dto.getProductId());
        detail.setQuantity(dto.getQuantity());
        detail.setSubtotal(unitPrice * dto.getQuantity());
        detail.setUnitPrice(unitPrice);

        OrderDetail saved = repository.save(detail);
        log.info("Order detail created successfully with id={}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    @Override
    public OrderDetailDTO getOrderDetailById(Long id) {
        log.debug("Fetching order detail with id={}", id);
        OrderDetail detail = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order detail with id={} not found", id);
                    return new OrderDetailNotFoundException(id);
                });
        return mapper.toResponseDto(detail);
    }

    @Override
    public Page<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId, Pageable pageable) {
        log.debug("Fetching order details for orderId={} with pagination", orderId);

        if (!orderClient.existsOrderById(orderId)) {
            log.error("Order with id={} not found", orderId);
            throw new InvalidOrderException(orderId);
        }

        return repository.findAllByOrderId(orderId, pageable)
                .map(mapper::toResponseDto);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        log.info("Attempting to delete order detail with id={}", id);

        OrderDetail detail = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order detail with id={} not found", id);
                    return new OrderDetailNotFoundException(id);
                });

        repository.delete(detail);
        log.info("Order detail with id={} deleted successfully", id);
    }
}