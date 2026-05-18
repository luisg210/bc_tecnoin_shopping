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
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private static final String STATUS_FINISH = "FINISH";

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;

    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderDTO dto, Long userId) {
        log.info("Creating order for customerId={}, userId={}", dto.getCustomerId(), userId);

        if (!customerClient.existsByUserId(userId)) {
            throw new CustomerNotFoundException(userId);
        }

        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setStatus(STATUS_PENDING);
        Order saved = repository.save(order);

        log.info("Order created with id={}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        log.debug("Fetching order with id={}", id);
        return mapper.toResponseDto(findOrderById(id));
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public void cancelOrder(Long id) {
        Order order = findOrderById(id);
        order.setStatus(STATUS_CANCELLED);
        repository.save(order);
        log.info("Order with id={} cancelled", id);
    }

    @Override
    @Transactional
    public void finishOrder(Long id) {
        Order order = findOrderById(id);
        order.setStatus(STATUS_FINISH);
        repository.save(order);
        log.info("Order with id={} finished", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        log.debug("Checking existence of order with id={}", id);
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public String getOrderStatus(Long id) {
        log.debug("Fetching status for order id={}", id);
        return findOrderById(id).getStatus();
    }

    private Order findOrderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order not found with id={}", id);
                    return new OrderNotFoundException(id);
                });
    }
}