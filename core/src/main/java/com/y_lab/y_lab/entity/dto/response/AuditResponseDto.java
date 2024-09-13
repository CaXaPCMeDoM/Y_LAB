package com.y_lab.y_lab.entity.dto.response;

import entity.ActionType;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record AuditResponseDto (
        @NotNull
        Long userId,
        @NotNull
        ActionType actionType,
        @NotNull
        Timestamp actionDate
){
}
