package com.y_lab.y_lab.web.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y_lab.y_lab.service.logger.AuditService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/audits/export")
public class AuditExportServlet extends HttpServlet {
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public AuditExportServlet() {
        auditService = ServiceContainer.getAuditService();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            auditService.exportLog();
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            Map<String, String> errorResponse = Map.of("error", "Failed to export audit logs.");
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(jsonResponse);
        }
    }
}
