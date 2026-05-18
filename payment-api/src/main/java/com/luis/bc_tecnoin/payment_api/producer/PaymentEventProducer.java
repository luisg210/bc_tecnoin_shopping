package com.luis.bc_tecnoin.payment_api.producer;

import com.luis.bc_tecnoin.payment_api.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentSuccess(PaymentEvent event) {
        log.info("Publishing PAYMENT_SUCCESS for orderId={}", event.getOrderId());
        kafkaTemplate.send("payments-topic", event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("PAYMENT_SUCCESS sent successfully for orderId={}, offset={}",
                                event.getOrderId(), result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send PAYMENT_SUCCESS for orderId={}", event.getOrderId(), ex);
                    }
                });
    }
}