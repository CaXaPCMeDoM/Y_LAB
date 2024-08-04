package com.y_lab.y_lab.service.logger.filter;

import com.y_lab.y_lab.entity.AuditEntity;

import java.util.List;

public interface Filter {
    List<AuditEntity> apply(List<AuditEntity> entities);
}
