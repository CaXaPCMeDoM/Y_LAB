package com.y_lab.y_lab.service.user.decorator.sort;

import com.y_lab.y_lab.entity.User;

import java.util.List;

public interface UserSorter {
    List<User> sort(List<User> users);
}
