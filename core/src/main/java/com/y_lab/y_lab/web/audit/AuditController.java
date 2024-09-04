package com.y_lab.y_lab.web.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.service.logger.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Export audit logs", description = "Exports the audit logs to a file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Export successful", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Failed to export audit logs", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/export")
    public ResponseEntity<String> exportAuditLogs() {
        try {
            auditService.exportLog();
            return new ResponseEntity<>("Export successful", HttpStatus.OK);
        } catch (IOException e) {
            String errorResponse = generateErrorResponse("Failed to export audit logs.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "View audit logs", description = "Retrieves a list of all audit logs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of audit logs retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuditEntity.class))),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve audit logs", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/view")
    public ResponseEntity<List<AuditEntity>> viewAuditLogs() {
        List<AuditEntity> auditEntityList = auditService.getAuditLogs();
        return new ResponseEntity<>(auditEntityList, HttpStatus.OK);
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
