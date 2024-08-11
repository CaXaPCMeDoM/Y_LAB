package com.y_lab.y_lab.service.logger.filter;

import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.entity.enums.ActionType;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AuditActionTypeFilter implements Filter {
    private ActionType actionType;

    @Override
    public List<AuditEntity> apply(List<AuditEntity> entities) {
        return entities.stream()
                .filter(entity -> entity.getActionType().equals(actionType))
                .collect(Collectors.toList());
    }
}
