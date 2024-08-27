package com.y_lab.y_lab.web.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.service.logger.AuditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/audits")
public class AuditController {
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public AuditController(AuditService auditService, ObjectMapper objectMapper) {
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/export")
    public ResponseEntity<String> exportAuditLogs() {
        try {
            auditService.exportLog();
            return ResponseEntity.ok("Export successful");
        } catch (IOException e) {
            String errorResponse = generateErrorResponse("Failed to export audit logs.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/view")
    public ResponseEntity<List<AuditEntity>> viewAuditLogs() {
        List<AuditEntity> auditEntityList = auditService.getAuditLogs();
        return ResponseEntity.ok(auditEntityList);
    }

    private String generateErrorResponse(String message) {
        try {
            Map<String, String> errorMap = Map.of("error", message);
            return objectMapper.writeValueAsString(errorMap);
        } catch (JsonProcessingException e) {
            return "{\"error\": \"An unexpected error occurred.\"}";
        }
    }
}
