package com.y_lab.y_lab.web.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.y_lab.y_lab.config.ServiceContainer;
import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.service.logger.AuditService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/audits/view")
public class AuditView extends HttpServlet {
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public AuditView() {
        auditService = ServiceContainer.getAuditService();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<AuditEntity> auditEntityList = auditService.getAuditLogs();

            String jsonResponse = objectMapper.writeValueAsString(auditEntityList);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");

            resp.getWriter().write(jsonResponse);
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
