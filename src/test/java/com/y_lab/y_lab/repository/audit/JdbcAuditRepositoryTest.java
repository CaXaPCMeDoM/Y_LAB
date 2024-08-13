package com.y_lab.y_lab.repository.audit;

import com.y_lab.y_lab.IntegrationEnvironment;
import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.entity.enums.ActionType;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcAuditRepositoryTest extends IntegrationEnvironment {
    private final  JdbcAuditRepository auditRepository = new JdbcAuditRepository(IntegrationEnvironment.connection);

    @Test
    void testSaveAuditLog() {
        // Arrange
        AuditEntity auditEntity = new AuditEntity(1L, ActionType.ADD_CAR, new Timestamp(System.currentTimeMillis()));

        // Act
        auditRepository.save(auditEntity);

        // Assert
        List<AuditEntity> logs = auditRepository.findAll();
        assertEquals(auditEntity.getUserId(), logs.get(0).getUserId());
        assertEquals(auditEntity.getActionType(), logs.get(0).getActionType());
    }

    @Test
    void testFindAllAuditLogs() {
        // Arrange
        AuditEntity auditEntity1 = new AuditEntity(1L, ActionType.ADD_CAR, new Timestamp(System.currentTimeMillis()));
        AuditEntity auditEntity2 = new AuditEntity(2L, ActionType.REMOVE_CAR, new Timestamp(System.currentTimeMillis()));
        auditRepository.save(auditEntity1);
        auditRepository.save(auditEntity2);

        // Act
        List<AuditEntity> logs = auditRepository.findAll();

        // Assert
        assertTrue(logs.contains(auditEntity1));
        assertTrue(logs.contains(auditEntity2));
    }

    @Test
    void testFilterAuditLogs() {
        // Arrange
        AuditEntity auditEntity1 = new AuditEntity(1L, ActionType.ADD_CAR, new Timestamp(System.currentTimeMillis()));
        AuditEntity auditEntity2 = new AuditEntity(2L, ActionType.REMOVE_CAR, new Timestamp(System.currentTimeMillis()));
        auditRepository.save(auditEntity1);
        auditRepository.save(auditEntity2);

        // Act
        List<AuditEntity> logs = auditRepository.findAll();
        List<AuditEntity> filteredLogs = logs.stream()
                .filter(log -> log.getActionType() == ActionType.ADD_CAR)
                .toList();

        // Assert
        assertEquals(1, filteredLogs.size());
        assertEquals(auditEntity1.getActionType(), filteredLogs.get(0).getActionType());
    }

    @Test
    void testSaveAndRetrieveMultipleAuditLogs() {
        // Arrange
        AuditEntity auditEntity1 = new AuditEntity(1L, ActionType.AUTHORIZATION, new Timestamp(System.currentTimeMillis()));
        AuditEntity auditEntity2 = new AuditEntity(2L, ActionType.LOGOUT, new Timestamp(System.currentTimeMillis()));
        auditRepository.save(auditEntity1);
        auditRepository.save(auditEntity2);

        // Act
        List<AuditEntity> logs = auditRepository.findAll();

        // Assert
        assertEquals(auditEntity1.getUserId(), logs.get(0).getUserId());
        assertEquals(auditEntity2.getUserId(), logs.get(1).getUserId());
    }
}