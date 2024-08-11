package com.y_lab.y_lab.service.user.decorator.filter;

import com.y_lab.y_lab.entity.User;

import java.util.List;

public interface UserFilter {
    List<User> filter(List<User> users);
}
