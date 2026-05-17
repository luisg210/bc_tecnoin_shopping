package com.luis.bc_tecnoin.order_api.service;

import com.luis.bc_tecnoin.order_api.clients.CustomerClient;
import com.luis.bc_tecnoin.order_api.dto.CreateOrderDTO;
import com.luis.bc_tecnoin.order_api.dto.OrderDTO;
import com.luis.bc_tecnoin.order_api.entity.Order;
import com.luis.bc_tecnoin.order_api.exception.CustomerNotFoundException;
import com.luis.bc_tecnoin.order_api.exception.OrderNotFoundException;
import com.luis.bc_tecnoin.order_api.mapper.OrderMapper;
import com.luis.bc_tecnoin.order_api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
/**
 * Service implementation for managing orders.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;

    @Override
    public OrderDTO createOrder(CreateOrderDTO dto, Long userId) {
        log.info("Creating order: {}", dto);

        // Validate customer existence
        if (!customerClient.existsByUserId(userId)) {
            throw new CustomerNotFoundException(userId);
        }

        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setStatus("PENDING");
        Order saved = repository.save(order);

        log.info("Order created: {}", saved);
        return mapper.toResponseDto(saved);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Order order = repository.findById(id)
                .orElseThrow(() ->
                {
                    log.error("Order not found with ID: {}", id);
                    return new OrderNotFoundException(id);
                });
        return mapper.toResponseDto(order);
    }


    @Override
    public Page<OrderDTO> getAllOrders(Long customerId, Pageable pageable) {
        log.debug("Fetching orders for customerId={} with pagination", customerId);

        if (!customerClient.existsByUserId(customerId)) {
            log.error("Customer with userId={} not found", customerId);
            throw new CustomerNotFoundException(customerId);
        }

        return repository.findAllByCustomerId(customerId, pageable)
                .map(mapper::toResponseDto);
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() ->
                {
                    log.error("Order not found with ID: {}", id);
                    return new OrderNotFoundException(id);
                });
        order.setStatus("CANCELLED");
        repository.save(order);
        log.info("Order with id: {} cancelled", id);
    }

    @Override
    public void finishOrder(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() ->
                {
                    log.error("Order not found with ID: {}", id);
                    return new OrderNotFoundException(id);
                });
        order.setStatus("FINISH");
        repository.save(order);
        log.info("Order with id: {} finish", id);
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Checking existence of order with id={}", id);
        return repository.existsById(id);
    }

    @Override
    public String getOrderStatus(Long id) {
        log.debug("Fetching status for order id={}", id);
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return order.getStatus();
    }
}