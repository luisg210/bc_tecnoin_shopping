package com.luis.bc_tecnoin.detail_api.mapper;

import com.luis.bc_tecnoin.detail_api.dto.CreateOrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.dto.OrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    @Mapping(source = "subtotal", target = "subtotal")
    OrderDetailDTO toResponseDto(OrderDetail entity);
    OrderDetail toEntity(CreateOrderDetailDTO dto);


}
