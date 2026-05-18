package com.luis.bc_tecnoin.payment_api.mapper;

import com.luis.bc_tecnoin.payment_api.dto.CreatePaymentDTO;
import com.luis.bc_tecnoin.payment_api.dto.PaymentDTO;
import com.luis.bc_tecnoin.payment_api.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDTO toResponseDto(Payment entity);
    Payment toEntity(CreatePaymentDTO dto);


}
