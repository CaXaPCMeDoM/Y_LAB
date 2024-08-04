package com.y_lab.y_lab.service.logger;

import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.entity.enums.ActionType;
import com.y_lab.y_lab.out.Exporter;
import com.y_lab.y_lab.service.logger.filter.Filter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AuditService {
    private static final List<AuditEntity> AUDIT_LOGS = new ArrayList<>();
    private final Exporter exporter;

    public AuditService(Exporter exporter) {
        this.exporter = exporter;
    }

    public void log(long userId, ActionType action) {
        AUDIT_LOGS.add(new AuditEntity(userId, action, new Timestamp(System.currentTimeMillis())));
    }

    public List<AuditEntity> getAuditLogs() {
        return new ArrayList<>(AUDIT_LOGS); // не знаю насколько это целесобразно с учетом
        // возможного веса логов, но давать возможность изменять их пользователю точно нельзя
    }

    public List<AuditEntity> filterLog(Filter filter) {
        return filter.apply(AUDIT_LOGS);
    }

    public void exportLog() throws IOException {
        exporter.export(AUDIT_LOGS);
    }
}
