package com.y_lab.y_lab.service;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.repository.user.UserRepository;
import com.y_lab.y_lab.service.user.decorator.filter.UserFilter;
import com.y_lab.y_lab.service.user.decorator.sort.UserSorter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import y_lab.annotaion.Loggable;

import java.util.List;

@Service
public class UserProfileService {
    private final UserRepository userRepository;
    @Setter
    private UserFilter userFilter;
    @Setter
    private UserSorter userSorter;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Loggable
    public List<User> getFilteredUsers() {
        List<User> users = userRepository.getUsers();

        if (userFilter != null) {
            users = userFilter.filter(users);
        }

        return users;
    }

    @Loggable
    public List<User> getSortedUsers() {
        List<User> users = userRepository.getUsers();

        if (userSorter != null) {
            users = userSorter.sort(users);
        }

        return users;
    }

    @Loggable
    public List<User> getFilteredAndSortedUsers() {
        List<User> users = getFilteredUsers();

        if (userSorter != null) {
            users = userSorter.sort(users);
        }

        return users;
    }
}
