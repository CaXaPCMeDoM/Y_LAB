package com.y_lab.y_lab.service.user.decorator.filter;

import com.y_lab.y_lab.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UsernameFilter implements UserFilter {
    private final String username;

    public UsernameFilter(String username) {
        this.username = username;
    }

    @Override
    public List<User> filter(List<User> users) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }
}
