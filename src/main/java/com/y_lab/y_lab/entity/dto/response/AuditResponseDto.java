package com.y_lab.y_lab.entity.dto.response;

import com.y_lab.y_lab.entity.enums.ActionType;

import javax.validation.constraints.NotNull;
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
