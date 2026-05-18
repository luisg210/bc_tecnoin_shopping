package com.luis.bc_tecnoin.order_api.consumer;

import com.luis.bc_tecnoin.order_api.event.PaymentEvent;
import com.luis.bc_tecnoin.order_api.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final IOrderService orderService;

    @KafkaListener(
            topics = "payments-topic",
            groupId = "order-service"
    )
    public void consumePaymentSuccess(PaymentEvent event) {
        log.info("Received PAYMENT_SUCCESS for orderId={}", event.getOrderId());
        if ("SUCCESS".equals(event.getStatus())) {
            orderService.finishOrder(event.getOrderId());
        }
    }
}
