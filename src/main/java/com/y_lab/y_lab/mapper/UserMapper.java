package com.y_lab.y_lab.mapper;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDto toUserResponseDto(User user);

    List<UserResponseDto> toUserResponseList(List<User> users);
}
