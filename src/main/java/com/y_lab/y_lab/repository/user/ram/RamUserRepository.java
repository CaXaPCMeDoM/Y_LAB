package com.y_lab.y_lab.repository.user.ram;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.repository.user.UserRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class RamUserRepository implements UserRepository {
    private static final List<User> users;
    @Getter
    @Setter
    private static long userIdCounter = 1;

    static {
        users = new ArrayList<>();
    }

    @Override
    public void add(User user) {
        user.setUserId(userIdCounter++);
        users.add(user);
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User findByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
