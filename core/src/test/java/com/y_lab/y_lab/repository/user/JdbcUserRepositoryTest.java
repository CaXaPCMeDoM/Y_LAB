package com.y_lab.y_lab.repository.user;

import com.y_lab.y_lab.IntegrationEnvironment;
import com.y_lab.y_lab.entity.User;
import entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUserRepositoryTest extends IntegrationEnvironment {
    private JdbcUserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        userRepository = new JdbcUserRepository(jdbcTemplate);
    }


    @Test
    public void testAddUser() {
        User user = new User(null, "testuser", "password", Role.USER);
        userRepository.add(user);

        assertNotNull(user.getUserId(), "User ID should not be null");

        User retrievedUser = userRepository.getFirstByUsername("testuser");
        assertNotNull(retrievedUser, "User should be retrieved");
        assertEquals("testuser", retrievedUser.getUsername(), "Username should match");
        assertEquals("password", retrievedUser.getPassword(), "Password should match");
        assertEquals(Role.USER, retrievedUser.getRole(), "Role should match");
    }

    @Test
    public void testGetUsers() {
        User user1 = new User(null, "user1", "password1", Role.ADMIN);
        User user2 = new User(null, "user2", "password2", Role.USER);
        userRepository.add(user1);
        userRepository.add(user2);

        List<User> users = userRepository.getUsers();

        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("user1")), "User1 should be in the list");
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("user2")), "User2 should be in the list");
    }

    @Test
    public void testFindByUsername() {
        User user = new User(null, "uniqueuser", "password", Role.USER);
        userRepository.add(user);

        User retrievedUser = userRepository.getFirstByUsername("uniqueuser");
        assertNotNull(retrievedUser, "User should be retrieved");
        assertEquals("uniqueuser", retrievedUser.getUsername(), "Username should match");
    }
}