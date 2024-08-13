package com.y_lab.y_lab.repository.audit;

import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.entity.enums.ActionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcAuditRepository implements AuditRepository {

    private final Connection connection;

    public JdbcAuditRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(AuditEntity auditEntity) {
        String sql = "INSERT INTO service.audit_log (user_id, action_type, action_date) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, auditEntity.getUserId());
            preparedStatement.setString(2, auditEntity.getActionType().name());
            preparedStatement.setTimestamp(3, auditEntity.getActionDate());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving audit log", e);
        }
    }

    @Override
    public List<AuditEntity> findAll() {
        String sql = "SELECT id, user_id, action_type, action_date FROM service.audit_log";
        List<AuditEntity> auditLogs = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                long userId = rs.getLong("user_id");
                ActionType actionType = ActionType.valueOf(rs.getString("action_type"));
                Timestamp actionDate = rs.getTimestamp("action_date");

                auditLogs.add(new AuditEntity(userId, actionType, actionDate));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching audit logs", e);
        }

        return auditLogs;
    }
}
