package com.y_lab.y_lab.mapper;

import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.entity.dto.response.AuditResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AuditMapper {
    AuditMapper INSTANCE = Mappers.getMapper(AuditMapper.class);
    AuditResponseDto toAuditResponseDto(AuditEntity auditEntity);
    List<AuditResponseDto> toAuditResponseDtoList(List<AuditEntity> auditEntities);
}
