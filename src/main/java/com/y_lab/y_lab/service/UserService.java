package com.y_lab.y_lab.service;

import com.y_lab.y_lab.annotation.Loggable;
import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.enums.ActionType;
import com.y_lab.y_lab.exception.AuthorizationFailedForTheRole;
import com.y_lab.y_lab.exception.InvalidUsernameOrPassword;
import com.y_lab.y_lab.exception.UserIsAlreadyRegistered;
import com.y_lab.y_lab.repository.user.UserRepository;
import com.y_lab.y_lab.security.UserContext;
import com.y_lab.y_lab.entity.UserInfo;
import com.y_lab.y_lab.service.autorization.chain.ChainOfRole;
import com.y_lab.y_lab.service.logger.AuditService;
import com.y_lab.y_lab.service.mapping.MappingUserAndUserInfo;
import com.y_lab.y_lab.service.user.decorator.filter.UserFilter;
import com.y_lab.y_lab.service.user.decorator.sort.UserSorter;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final ChainOfRole chainOfRole;
    private final UserProfileService userProfileService;

    public UserService(UserRepository userRepository, AuditService auditService) {
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.userProfileService = new UserProfileService(userRepository);
        this.chainOfRole = new ChainOfRole();
    }

    @Loggable(action_type = ActionType.REGISTRATION)
    public void registerUser(User user) throws AuthorizationFailedForTheRole, UserIsAlreadyRegistered {
        if (!chainOfRole.assemblingTheChain(user.getRole())) { // если роль не соответствует пользователю
            throw new AuthorizationFailedForTheRole();
        } else if (userRepository.findByUsername(user.getUsername()) == null) {
            userRepository.add(user);

            UserInfo userInfo = MappingUserAndUserInfo.userToUserInfo(user);
            UserContext.setCurrentUser(userInfo);
        } else { // если уже зарегистрирован
            throw new UserIsAlreadyRegistered();
        }
    }

    @Loggable(action_type = ActionType.AUTHORIZATION)
    public User authorization(String username, String password) throws InvalidUsernameOrPassword {
        User user = userRepository.findByUsername(username);
        if (user == null || (!password.equals(user.getPassword()))) { // Не прошла авторизация
            throw new InvalidUsernameOrPassword();
        } else {
            UserInfo userInfo = MappingUserAndUserInfo.userToUserInfo(user);
            UserContext.setCurrentUser(userInfo);
            return user;
        }
    }

    @Loggable(action_type = ActionType.LOGOUT)
    public void logout() {
        UserContext.clear();
    }

    public List<User> getAll() {
        return userRepository.getUsers();
    }

    public List<User> getFilteredUsers(UserFilter userFilter) {
        userProfileService.setUserFilter(userFilter);
        return userProfileService.getFilteredUsers();
    }

    public List<User> getSortedUsers(UserSorter userSorter) {
        userProfileService.setUserSorter(userSorter);
        return userProfileService.getSortedUsers();
    }

    public List<User> getFilteredAndSortedUsers(UserFilter userFilter, UserSorter userSorter) {
        userProfileService.setUserFilter(userFilter);
        userProfileService.setUserSorter(userSorter);
        return userProfileService.getFilteredAndSortedUsers();
    }
}
