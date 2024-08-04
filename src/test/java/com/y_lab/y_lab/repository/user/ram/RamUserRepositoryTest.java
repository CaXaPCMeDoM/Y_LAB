package com.y_lab.y_lab.repository.user.ram;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RamUserRepositoryTest {
    private RamUserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = new RamUserRepository();
        RamUserRepository.setUserIdCounter(1L);
        userRepository.getUsers().clear();
    }

    @Test
    public void testAddUser() {
        User user = new User(null, "testuser", "password", Role.USER);
        userRepository.add(user);

        assertNotNull(user.getUserId());
        assertEquals(1L, user.getUserId());
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testGetUsers() {
        User user1 = new User(null, "testuser1", "password1", Role.USER);
        userRepository.add(user1);

        User user2 = new User(null, "testuser2", "password2", Role.USER);
        userRepository.add(user2);

        List<User> users = userRepository.getUsers();
        assertEquals(2, users.size());
        assertEquals("testuser1", users.get(0).getUsername());
        assertEquals("testuser2", users.get(1).getUsername());
    }

    @Test
    public void testFindByUsername() {
        User user1 = new User(null, "testuser1", "password1", Role.USER);
        userRepository.add(user1);

        User user2 = new User(null, "testuser2", "password2", Role.USER);
        userRepository.add(user2);

        User foundUser = userRepository.findByUsername("testuser1");
        assertNotNull(foundUser);
        assertEquals("testuser1", foundUser.getUsername());

        User notFoundUser = userRepository.findByUsername("nonexistentuser");
        assertNull(notFoundUser);
    }
}
