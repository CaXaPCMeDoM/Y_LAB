package com.y_lab.y_lab.service;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.repository.user.UserRepository;
import com.y_lab.y_lab.repository.user.ram.RamUserRepository;
import com.y_lab.y_lab.service.user.decorator.filter.UserFilter;
import com.y_lab.y_lab.service.user.decorator.sort.UserSorter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserProfileService {
    private final UserRepository userRepository;
    @Setter
    private UserFilter userFilter;
    @Setter
    private UserSorter userSorter;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getFilteredUsers() {
        List<User> users = userRepository.getUsers();

        if (userFilter != null) {
            users = userFilter.filter(users);
        }

        return users;
    }

    public List<User> getSortedUsers() {
        List<User> users = userRepository.getUsers();

        if (userSorter != null) {
            users = userSorter.sort(users);
        }

        return users;
    }

    public List<User> getFilteredAndSortedUsers() {
        List<User> users = getFilteredUsers();

        if (userSorter != null) {
            users = userSorter.sort(users);
        }

        return users;
    }
}
