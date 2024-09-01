package com.y_lab.y_lab.repository.user;

import com.y_lab.y_lab.entity.User;

import java.util.List;

public interface UserRepository {
    void add(User user);
    List<User> getUsers();
    User getFirstByUsername(String username);
}
