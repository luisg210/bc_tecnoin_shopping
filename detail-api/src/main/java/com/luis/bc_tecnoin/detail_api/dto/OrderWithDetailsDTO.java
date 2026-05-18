package com.luis.bc_tecnoin.detail_api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderWithDetailsDTO {
    private OrderDTO order;
    private List<OrderDetailDTO> details;
    private Double total;
}
