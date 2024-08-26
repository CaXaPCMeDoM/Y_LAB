package com.y_lab.y_lab.repository.audit;

import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.entity.enums.ActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcAuditRepository implements AuditRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<AuditEntity> AUDIT_ROW_MAPPER = (rs, rowNum) ->
            new AuditEntity(
                    rs.getLong("user_id"),
                    ActionType.valueOf(rs.getString("action_type")),
                    rs.getTimestamp("action_date")
            );

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void save(AuditEntity auditEntity) {
        String sql = "INSERT INTO service.audit_log (user_id, action_type, action_date) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                    auditEntity.getUserId(),
                    auditEntity.getActionType().name(),
                    auditEntity.getActionDate());
        } catch (Exception e) {
            throw new RuntimeException("Error saving audit log", e);
        }
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<AuditEntity> findAll() {
        String sql = "SELECT id, user_id, action_type, action_date FROM service.audit_log";
        try {
            return jdbcTemplate.query(sql, AUDIT_ROW_MAPPER);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching audit logs", e);
        }
    }
}
