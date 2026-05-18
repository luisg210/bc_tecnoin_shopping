package com.luis.bc_tecnoin.payment_api.service;

import com.luis.bc_tecnoin.payment_api.clients.OrderClient;
import com.luis.bc_tecnoin.payment_api.clients.OrderDetailClient;
import com.luis.bc_tecnoin.payment_api.dto.CreatePaymentDTO;
import com.luis.bc_tecnoin.payment_api.dto.PaymentDTO;
import com.luis.bc_tecnoin.payment_api.entity.Payment;
import com.luis.bc_tecnoin.payment_api.exception.InvalidPaymentException;
import com.luis.bc_tecnoin.payment_api.exception.PaymentNotFoundException;
import com.luis.bc_tecnoin.payment_api.mapper.PaymentMapper;
import com.luis.bc_tecnoin.payment_api.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing order details.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImp implements IPaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final OrderClient orderClient;
    private final OrderDetailClient orderDetailClient;


    @Override
    public PaymentDTO processPayment(CreatePaymentDTO dto) {
        log.info("Processing payment for orderId={}", dto.getOrderId());

        // Validate order existence
        if (!orderClient.existsOrderById(dto.getOrderId())) {
            log.error("Order with id={} not found", dto.getOrderId());
            throw new InvalidPaymentException(dto.getOrderId(), "Order does not exist");
        }

        // Validate order status
        String status = orderClient.getOrderStatus(dto.getOrderId());
        if (!"PENDING".equals(status)) {
            log.error("Order with id={} is not PENDING, current status={}", dto.getOrderId(), status);
            throw new InvalidPaymentException(dto.getOrderId(), "Order is not in PENDING state");
        }

        // Calculate total from order details
        Double totalAmount = orderDetailClient.calculateTotal(dto.getOrderId());
        log.debug("Calculated total amount={} for orderId={}", totalAmount, dto.getOrderId());

        // Simulate payment process
        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        payment.setAmount(totalAmount);
        payment.setStatus("SUCCESS");

        Payment saved = repository.save(payment);
        log.info("Payment processed successfully with id={} for orderId={}", saved.getId(), dto.getOrderId());

        return mapper.toResponseDto(saved);
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        log.debug("Fetching payment with id={}", id);
        Payment payment = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment with id={} not found", id);
                    return new PaymentNotFoundException(id);
                });
        return mapper.toResponseDto(payment);
    }
}