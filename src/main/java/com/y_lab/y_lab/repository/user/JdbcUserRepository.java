package com.y_lab.y_lab.repository.user;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.enums.Role;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdbcUserRepository implements UserRepository {
    private final Connection connection;

    public JdbcUserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(User user) {
        String sql = "INSERT INTO entity_schema.user (username, password, role) VALUES (?, ?, ?) RETURNING user_id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user.setUserId(resultSet.getLong("user_id"));
            }
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM entity_schema.user";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM entity_schema.user WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToUser(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error SQL query");
            throw new RuntimeException(e);
        }
        return null;
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("user_id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }
}
