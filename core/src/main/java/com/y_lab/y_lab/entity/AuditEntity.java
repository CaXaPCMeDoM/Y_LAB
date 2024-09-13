package com.y_lab.y_lab.entity;

import entity.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditEntity {
    private Long userId;
    private ActionType actionType;
    private Timestamp actionDate;
}
