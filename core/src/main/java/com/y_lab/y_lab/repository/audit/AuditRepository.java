package com.y_lab.y_lab.repository.audit;

import com.y_lab.y_lab.entity.AuditEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository {
    void save(AuditEntity auditEntity);

    List<AuditEntity> findAll();
}
