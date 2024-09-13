package com.y_lab.y_lab.service.logger;

import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.out.Exporter;
import com.y_lab.y_lab.repository.audit.AuditRepository;
import com.y_lab.y_lab.service.logger.filter.Filter;
import entity.ActionType;
import org.spring.boot.starter.my.service.LogService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class AuditService implements LogService {

    private final AuditRepository auditRepository;
    private final Exporter exporter;

    public AuditService(AuditRepository auditRepository, Exporter exporter) {
        this.auditRepository = auditRepository;
        this.exporter = exporter;
    }

    @Override
    public void log(long userId, ActionType action) {
        AuditEntity auditEntity = new AuditEntity(userId, action, new Timestamp(System.currentTimeMillis()));
        auditRepository.save(auditEntity);
    }

    public List<AuditEntity> getAuditLogs() {
        return auditRepository.findAll();
    }

    public List<AuditEntity> filterLog(Filter filter) {
        List<AuditEntity> auditLogs = auditRepository.findAll();
        return filter.apply(auditLogs);
    }

    public void exportLog() throws IOException {
        List<AuditEntity> auditLogs = auditRepository.findAll();
        exporter.export(auditLogs);
    }
}
