package com.y_lab.y_lab.entity.dto.response;

import com.y_lab.y_lab.entity.enums.ActionType;

import java.sql.Timestamp;

public record AuditResponseDto (
        Long userId,
        ActionType actionType,
        Timestamp actionDate
){
}
