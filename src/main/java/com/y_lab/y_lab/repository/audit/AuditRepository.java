package com.y_lab.y_lab.repository.audit;

import com.y_lab.y_lab.entity.AuditEntity;

import java.util.List;

public interface AuditRepository {
    void save(AuditEntity auditEntity);

    List<AuditEntity> findAll();
}
