package com.y_lab.y_lab.repository.user;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            new User(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("role"))
            );

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(User user) {
        String sql = "INSERT INTO entity_schema.user (username, password, role) VALUES (?, ?, ?) RETURNING user_id";
        try {
            Long userId = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{user.getUsername(), user.getPassword(), user.getRole().name()},
                    Long.class
            );
            if (userId != null) {
                user.setUserId(userId);
            }
        } catch (Exception e) {
            log.error("Error executing SQL query", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM entity_schema.user";
        try {
            return jdbcTemplate.query(sql, USER_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Error executing SQL query", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM entity_schema.user WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, USER_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Error executing SQL query", e);
            throw new RuntimeException(e);
        }
    }
}
