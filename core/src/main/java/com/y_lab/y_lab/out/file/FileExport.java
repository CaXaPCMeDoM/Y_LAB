package com.y_lab.y_lab.out.file;

import com.y_lab.y_lab.entity.AuditEntity;
import com.y_lab.y_lab.out.Exporter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileExport implements Exporter {
    private final String filename;

    public FileExport(String filename) {
        this.filename = filename;
    }

    @Override
    public void export(List<AuditEntity> entities) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (AuditEntity entry : entities) {
                writer.write(entry.toString());
                writer.newLine();
            }
        }
    }
}
