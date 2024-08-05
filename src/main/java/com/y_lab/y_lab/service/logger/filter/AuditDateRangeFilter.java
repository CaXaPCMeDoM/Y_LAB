package com.y_lab.y_lab.service.logger.filter;

import com.y_lab.y_lab.entity.AuditEntity;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AuditDateRangeFilter implements Filter {
    private Timestamp startDate;
    private Timestamp endDate;

    @Override
    public List<AuditEntity> apply(List<AuditEntity> entities) {
        return entities.stream()
                .filter(entry ->
                        !entry.getActionDate().before(startDate)
                                && !entry.getActionDate().after(endDate))
                // нельзя без логического "не", так как нужно "включить" концы
                .collect(Collectors.toList());
    }
}
