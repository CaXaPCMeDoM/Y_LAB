package com.y_lab.y_lab.repository.user;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_USER_SQL = "INSERT INTO entity_schema.user (username, password, role) VALUES (?, ?, ?) RETURNING user_id";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM entity_schema.user";
    private static final String SELECT_USER_BY_USERNAME_SQL = "SELECT * FROM entity_schema.user WHERE username = ?";

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
        try {
            Long userId = jdbcTemplate.queryForObject(
                    INSERT_USER_SQL,
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
        try {
            return jdbcTemplate.query(SELECT_ALL_USERS_SQL, USER_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Error executing SQL query", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME_SQL, USER_ROW_MAPPER, username);
        } catch (Exception e) {
            log.error("Error executing SQL query", e);
            throw new RuntimeException(e);
        }
    }
}
