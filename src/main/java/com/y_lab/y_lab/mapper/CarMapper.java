package com.y_lab.y_lab.mapper;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.dto.response.CarResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {
    CarResponseDto toCarResponseDto(Car car);

    List<CarResponseDto> toCarResponseDtoList(List<Car> cars);
}
