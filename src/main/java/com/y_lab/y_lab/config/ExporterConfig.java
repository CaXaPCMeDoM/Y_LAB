package com.y_lab.y_lab.config;

import com.y_lab.y_lab.out.Exporter;
import com.y_lab.y_lab.out.file.FileExport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExporterConfig {
    @Bean
    public Exporter exporterService(@Value("${app.audit.export.filename}") String filename) {
        return new FileExport(filename);
    }
}
