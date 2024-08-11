package com.y_lab.y_lab.service.logger.filter;

import com.y_lab.y_lab.entity.AuditEntity;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AuditUserFilter implements Filter {
    private long userId;

    @Override
    public List<AuditEntity> apply(List<AuditEntity> entities) {
        return entities.stream()
                .filter(entity -> entity.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
