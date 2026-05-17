package com.luis.bc_tecnoin.order_api.mapper;

import com.luis.bc_tecnoin.order_api.dto.CreateOrderDTO;
import com.luis.bc_tecnoin.order_api.dto.OrderDTO;
import com.luis.bc_tecnoin.order_api.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toResponseDto(Order entity);
    Order toEntity(CreateOrderDTO dto);


}
