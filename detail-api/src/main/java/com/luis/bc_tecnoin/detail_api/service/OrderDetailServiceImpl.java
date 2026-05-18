package com.luis.bc_tecnoin.detail_api.service;

import com.luis.bc_tecnoin.detail_api.clients.OrderClient;
import com.luis.bc_tecnoin.detail_api.clients.ProductClient;
import com.luis.bc_tecnoin.detail_api.dto.CreateOrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.dto.OrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.dto.OrderDTO;
import com.luis.bc_tecnoin.detail_api.dto.OrderWithDetailsDTO;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements IOrderDetailService {

    private final OrderDetailRepository repository;
    private final OrderDetailMapper mapper;
    private final OrderClient orderClient;
    private final ProductClient productClient;

    @Override
    @Transactional
    public OrderDetailDTO createOrderDetail(CreateOrderDetailDTO dto) {
        log.info("Creating order detail for orderId={}", dto.getOrderId());

        if (!orderClient.existsOrderById(dto.getOrderId())) {
            log.error("Order with id={} not found", dto.getOrderId());
            throw new InvalidOrderException(dto.getOrderId());
        }

        ProductDTO product = productClient.getById(dto.getProductId());
        if (product == null) {
            log.error("Product with id={} not found", dto.getProductId());
            throw new ProductNotFoundException(dto.getProductId());
        }

        Double unitPrice = product.getPrice();
        log.debug("Product price={} for productId={}", unitPrice, dto.getProductId());

        OrderDetail detail = new OrderDetail();
        detail.setOrderId(dto.getOrderId());
        detail.setProductId(dto.getProductId());
        detail.setQuantity(dto.getQuantity());
        detail.setUnitPrice(unitPrice);
        detail.setSubtotal(unitPrice * dto.getQuantity());

        OrderDetail saved = repository.save(detail);
        log.info("Order detail created with id={}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailDTO getOrderDetailById(Long id) {
        log.debug("Fetching order detail with id={}", id);
        return mapper.toResponseDto(findDetailById(id));
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public void deleteOrderDetail(Long id) {
        log.info("Deleting order detail with id={}", id);
        repository.delete(findDetailById(id));
        log.info("Order detail with id={} deleted", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        log.debug("Checking existence of order detail with id={}", id);
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateTotal(Long orderId) {
        log.debug("Calculating total for orderId={}", orderId);

        if (!repository.existsByOrderId(orderId)) {
            log.error("Order with id={} not found", orderId);
            throw new InvalidOrderException(orderId);
        }

        return repository.findAllByOrderId(orderId, Pageable.unpaged())
                .stream()
                .mapToDouble(OrderDetail::getSubtotal)
                .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderWithDetailsDTO getOrderWithDetails(Long orderId) {
        log.info("Fetching order with details for orderId={}", orderId);

        OrderDTO order = orderClient.getOrderById(orderId);
        if (order == null) {
            log.error("Order with id={} not found", orderId);
            throw new InvalidOrderException(orderId);
        }

        Page<OrderDetailDTO> detailsPage = getOrderDetailsByOrderId(orderId, Pageable.unpaged());
        Double total = detailsPage.getContent().stream()
                .mapToDouble(OrderDetailDTO::getSubtotal)
                .sum();

        OrderWithDetailsDTO result = new OrderWithDetailsDTO();
        result.setOrder(order);
        result.setDetails(detailsPage.getContent());
        result.setTotal(total);
        return result;
    }

    private OrderDetail findDetailById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order detail with id={} not found", id);
                    return new OrderDetailNotFoundException(id);
                });
    }
}