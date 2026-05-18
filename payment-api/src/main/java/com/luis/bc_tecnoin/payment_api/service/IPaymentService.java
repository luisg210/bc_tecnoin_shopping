package com.luis.bc_tecnoin.payment_api.service;

import com.luis.bc_tecnoin.payment_api.dto.CreatePaymentDTO;
import com.luis.bc_tecnoin.payment_api.dto.PaymentDTO;

public interface IPaymentService {
    PaymentDTO processPayment(CreatePaymentDTO dto);
    PaymentDTO getPaymentById(Long id);
}