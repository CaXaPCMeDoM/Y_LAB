package com.y_lab.y_lab.entity;

import com.y_lab.y_lab.entity.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class AuditEntity {
    private Long userId;
    private ActionType actionType;
    private Timestamp actionDate;
}
