package com.y_lab.y_lab.repository.audit;

import com.y_lab.y_lab.entity.AuditEntity;

import java.util.List;

public interface AuditRepository {
    public void save(AuditEntity auditEntity);

    public List<AuditEntity> findAll();
}
