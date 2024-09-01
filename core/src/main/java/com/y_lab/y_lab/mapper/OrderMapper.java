package com.y_lab.y_lab.mapper;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.dto.response.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderResponseDto toOrderResponseDto(Order order);

    List<OrderResponseDto> toOrderResponseDtoList(List<Order> orders);
}
