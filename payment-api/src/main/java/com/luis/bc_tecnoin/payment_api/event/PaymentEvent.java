package com.luis.bc_tecnoin.payment_api.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private Long orderId;
    private String status;     // SUCCESS, FAILED
    private String timestamp;
}