package com.y_lab.y_lab.mapper;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.dto.response.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResponseDto toOrderResponseDto(Order order);

    List<OrderResponseDto> toOrderResponseDtoList(List<Order> orders);
}
