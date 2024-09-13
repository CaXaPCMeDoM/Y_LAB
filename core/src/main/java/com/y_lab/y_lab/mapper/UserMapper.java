package com.y_lab.y_lab.mapper;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);

    List<UserResponseDto> toUserResponseList(List<User> users);
}
