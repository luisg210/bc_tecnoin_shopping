package com.luis.bc_tecnoin.payment_api.service;

import com.luis.bc_tecnoin.payment_api.clients.OrderClient;
import com.luis.bc_tecnoin.payment_api.clients.OrderDetailClient;
import com.luis.bc_tecnoin.payment_api.dto.CreatePaymentDTO;
import com.luis.bc_tecnoin.payment_api.dto.PaymentDTO;
import com.luis.bc_tecnoin.payment_api.entity.Payment;
import com.luis.bc_tecnoin.payment_api.event.PaymentEvent;
import com.luis.bc_tecnoin.payment_api.exception.InvalidPaymentException;
import com.luis.bc_tecnoin.payment_api.exception.PaymentNotFoundException;
import com.luis.bc_tecnoin.payment_api.mapper.PaymentMapper;
import com.luis.bc_tecnoin.payment_api.producer.PaymentEventProducer;
import com.luis.bc_tecnoin.payment_api.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImp implements IPaymentService {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_FAILED = "FAILED";
    private static final List<String> MOCK_STATUSES = List.of(STATUS_SUCCESS, STATUS_FAILED);
    private static final Random RANDOM = new Random();

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final OrderClient orderClient;
    private final OrderDetailClient orderDetailClient;
    private final PaymentEventProducer eventProducer;

    @Override
    @Transactional
    public PaymentDTO processPayment(CreatePaymentDTO dto) {
        log.info("Processing payment for orderId={}", dto.getOrderId());

        validateOrder(dto.getOrderId());

        Double totalAmount = orderDetailClient.calculateTotal(dto.getOrderId());
        log.debug("Calculated total amount={} for orderId={}", totalAmount, dto.getOrderId());

        Payment payment = mockPayment(dto.getOrderId(), totalAmount);
        Payment saved = repository.save(payment);
        log.info("Payment processed successfully with id={} for orderId={}", saved.getId(), dto.getOrderId());

        if (Objects.equals(saved.getStatus(), STATUS_SUCCESS)) {
            PaymentEvent event = new PaymentEvent(dto.getOrderId(), STATUS_SUCCESS, Instant.now().toString());
            log.info("Publishing event payment event={}", event);
            eventProducer.publishPaymentSuccess(event);
        }

        return mapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long id) {
        log.debug("Fetching payment with id={}", id);
        Payment payment = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment with id={} not found", id);
                    return new PaymentNotFoundException(id);
                });
        return mapper.toResponseDto(payment);
    }

    private void validateOrder(Long orderId) {
        if (!orderClient.existsOrderById(orderId)) {
            log.error("Order with id={} not found", orderId);
            throw new InvalidPaymentException(orderId, "Order does not exist");
        }
        String status = orderClient.getOrderStatus(orderId);
        if (!STATUS_PENDING.equals(status)) {
            log.error("Order with id={} is not PENDING, current status={}", orderId, status);
            throw new InvalidPaymentException(orderId, "Order is not in PENDING state");
        }
    }

    private Payment mockPayment(Long orderId, Double totalAmount) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(totalAmount);
        payment.setStatus(MOCK_STATUSES.get(RANDOM.nextInt(MOCK_STATUSES.size())));
        return payment;
    }
}