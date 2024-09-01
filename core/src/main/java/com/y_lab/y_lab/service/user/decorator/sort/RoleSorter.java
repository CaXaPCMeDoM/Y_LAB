package com.y_lab.y_lab.service.user.decorator.sort;

import com.y_lab.y_lab.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RoleSorter implements UserSorter{
    @Override
    public List<User> sort(List<User> users) {
        return users.stream()
                .sorted(Comparator.comparing(user -> user.getRole().toString()))
                .collect(Collectors.toList());
    }
}
