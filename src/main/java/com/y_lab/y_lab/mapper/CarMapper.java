package com.y_lab.y_lab.mapper;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.dto.response.CarResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    CarResponseDto toCarResponseDto(Car car);

    List<CarResponseDto> toCarResponseDtoList(List<Car> cars);
}
