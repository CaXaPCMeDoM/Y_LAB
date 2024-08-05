package com.y_lab.y_lab.starter.manager;

import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.out.Printer;
import com.y_lab.y_lab.service.logger.AuditService;

import java.io.IOException;
import java.util.List;

/**
 * Класс AuditManagement предоставляет функциональность для просмотра аудиторских журналов.
 */
public class AuditManagement {
    private final AuditService auditService;
    private final Printer printer;

    /**
     * Создает новый экземпляр AuditManagement с указанными сервисом аудита и утилитой вывода.
     *
     * @param auditService сервис для работы с аудиторскими журналами
     * @param printer      утилита для вывода сообщений пользователю
     */
    public AuditManagement(AuditService auditService, Printer printer) {
        this.auditService = auditService;
        this.printer = printer;
    }

    /**
     * Отображает все записи аудиторских журналов.
     * Запрашивает у auditService все записи и выводит их с помощью printer.
     */
    public void viewAuditLog() {
        List<AuditEntity> logs = auditService.getAuditLogs();
        for (AuditEntity log : logs) {
            printer.print(log.toString());
        }
    }

    /**
     * Экспортирует все записи аудиторских журналов.
     */
    public void exportAuditLog() {
        try {
            auditService.exportLog();
        } catch (IOException e) {
        }
    }
}
