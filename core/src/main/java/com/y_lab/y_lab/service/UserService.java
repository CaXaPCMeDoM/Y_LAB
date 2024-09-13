package com.y_lab.y_lab.service;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.exception.AuthorizationFailedForTheRole;
import com.y_lab.y_lab.exception.InvalidUsernameOrPassword;
import com.y_lab.y_lab.exception.UserIsAlreadyRegistered;
import com.y_lab.y_lab.repository.user.UserRepository;
import com.y_lab.y_lab.service.autorization.chain.ChainOfRole;
import com.y_lab.y_lab.service.mapping.MappingUserAndUserInfo;
import com.y_lab.y_lab.service.user.decorator.filter.UserFilter;
import com.y_lab.y_lab.service.user.decorator.sort.UserSorter;
import context.UserContext;
import entity.ActionType;
import entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.spring.boot.starter.my.annotation.Auditable;
import org.springframework.stereotype.Service;
import y_lab.annotaion.Loggable;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ChainOfRole chainOfRole;
    private final UserProfileService userProfileService;

    @Auditable(action_type = ActionType.REGISTRATION)
    @Loggable
    public void registerUser(User user) throws AuthorizationFailedForTheRole, UserIsAlreadyRegistered {
        if (!chainOfRole.assemblingTheChain(user.getRole())) { // если роль не соответствует пользователю
            throw new AuthorizationFailedForTheRole();
        } else if (userRepository.getFirstByUsername(user.getUsername()) == null) {
            userRepository.add(user);

            UserInfo userInfo = MappingUserAndUserInfo.userToUserInfo(user);
            UserContext.setCurrentUser(userInfo);
        } else { // если уже зарегистрирован
            throw new UserIsAlreadyRegistered();
        }
    }

    @Auditable(action_type = ActionType.AUTHORIZATION)
    @Loggable
    public User authorization(String username, String password) throws InvalidUsernameOrPassword {
        User user = userRepository.getFirstByUsername(username);
        if (user == null || (!password.equals(user.getPassword()))) { // Не прошла авторизация
            throw new InvalidUsernameOrPassword();
        } else {
            UserInfo userInfo = MappingUserAndUserInfo.userToUserInfo(user);
            UserContext.setCurrentUser(userInfo);
            return user;
        }
    }

    @Auditable(action_type = ActionType.LOGOUT)
    @Loggable
    public void logout() {
        UserContext.clear();
    }

    @Loggable
    public List<User> getAll() {
        return userRepository.getUsers();
    }

    @Loggable
    public List<User> getFilteredUsers(UserFilter userFilter) {
        userProfileService.setUserFilter(userFilter);
        return userProfileService.getFilteredUsers();
    }

    @Loggable
    public List<User> getSortedUsers(UserSorter userSorter) {
        userProfileService.setUserSorter(userSorter);
        return userProfileService.getSortedUsers();
    }

    @Loggable
    public List<User> getFilteredAndSortedUsers(UserFilter userFilter, UserSorter userSorter) {
        userProfileService.setUserFilter(userFilter);
        userProfileService.setUserSorter(userSorter);
        return userProfileService.getFilteredAndSortedUsers();
    }
}
