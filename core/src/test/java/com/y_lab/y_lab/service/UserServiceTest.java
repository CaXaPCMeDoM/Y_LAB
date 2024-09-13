package com.y_lab.y_lab.service;

import com.y_lab.y_lab.service.logger.AuditService;
import context.UserContext;
import entity.UserInfo;
import entity.enums.Role;
import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.exception.AuthorizationFailedForTheRole;
import com.y_lab.y_lab.exception.InvalidUsernameOrPassword;
import com.y_lab.y_lab.repository.user.UserRepository;
import com.y_lab.y_lab.service.autorization.chain.ChainOfRole;
import com.y_lab.y_lab.service.mapping.MappingUserAndUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    private UserRepository userRepository;
    private AuditService auditService;
    private UserService userService;
    private ChainOfRole chainOfRole;
    private UserProfileService userProfileService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        auditService = mock(AuditService.class);
        chainOfRole = mock(ChainOfRole.class);
        userProfileService = mock(UserProfileService.class);
        userService = new UserService(userRepository, chainOfRole, userProfileService);
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        // Arrange
        User user = new User(1L, "testuser", "password", Role.USER);
        when(userRepository.getFirstByUsername(user.getUsername())).thenReturn(null);
        when(chainOfRole.assemblingTheChain(user.getRole())).thenReturn(true);
        // Act
        userService.registerUser(user);

        // Assert
        verify(userRepository).add(user);
        assertNotNull(UserContext.getCurrentUser());
    }

    @Test
    public void testRegisterUser_FailAuthorization() {
        // Arrange
        User user = new User(null, "testuser", "password", Role.USER);
        when(userRepository.getFirstByUsername(user.getUsername())).thenReturn(null);
        when(chainOfRole.assemblingTheChain(user.getRole())).thenReturn(false);

        // Act & Assert
        assertThrows(AuthorizationFailedForTheRole.class, () -> userService.registerUser(user));
    }

    @Test
    public void testAuthorization_Success() throws Exception {
        // Arrange
        User user = new User(1L, "testuser", "password", Role.USER);
        when(userRepository.getFirstByUsername(user.getUsername())).thenReturn(user);

        // Act
        User result = userService.authorization(user.getUsername(), user.getPassword());

        // Assert
        assertEquals(user, result);
        assertNotNull(UserContext.getCurrentUser());
    }

    @Test
    public void testAuthorization_FailInvalidPassword() {
        // Arrange
        User user = new User(1L, "testuser", "password", Role.USER);
        when(userRepository.getFirstByUsername(user.getUsername())).thenReturn(user);

        // Act & Assert
        assertThrows(InvalidUsernameOrPassword.class, () -> userService.authorization(user.getUsername(), "wrongpassword"));
    }

    @Test
    public void testAuthorization_FailUserNotFound() {
        // Arrange
        when(userRepository.getFirstByUsername("testuser")).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidUsernameOrPassword.class, () -> userService.authorization("testuser", "password"));
    }

    @Test
    public void testLogout() {
        // Arrange
        User user = new User(1L, "testuser", "password", Role.USER);
        UserInfo userInfo = MappingUserAndUserInfo.userToUserInfo(user);
        UserContext.setCurrentUser(userInfo);

        // Act
        userService.logout();

        // Assert
        assertNull(UserContext.getCurrentUser());
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        User user = new User(1L, "testuser", "password", Role.USER);
        users.add(user);
        when(userRepository.getUsers()).thenReturn(users);

        // Act
        List<User> result = userService.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }
}