package com.y_lab.y_lab.service.user.decorator.sort;

import com.y_lab.y_lab.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UsernameSorter implements UserSorter{
    @Override
    public List<User> sort(List<User> users) {
        return users.stream()
                .sorted(Comparator.comparing(User::getUsername))
                .collect(Collectors.toList());
    }
}
