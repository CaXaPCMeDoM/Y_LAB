package com.y_lab.y_lab.starter.manager;

import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.out.Printer;
import com.y_lab.y_lab.service.logger.AuditService;

import java.util.List;

public class AuditManagement {
    private final AuditService auditService;
    private final Printer printer;

    public AuditManagement(AuditService auditService, Printer printer) {
        this.auditService = auditService;
        this.printer = printer;
    }

    public void viewAuditLog() {
        List<AuditEntity> logs = auditService.getAuditLogs();
        for (AuditEntity log : logs) {
            printer.print(log.toString());
        }
    }
}
