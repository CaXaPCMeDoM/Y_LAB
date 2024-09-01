package com.y_lab.y_lab.service.mapping;

import com.y_lab.y_lab.entity.User;
import entity.UserInfo;

public class MappingUserAndUserInfo {
    public static UserInfo userToUserInfo(User user) {
        return new UserInfo(
                user.getUserId(),
                user.getUsername(),
                user.getRole());
    }
}
