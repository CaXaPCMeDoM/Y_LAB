package com.y_lab.y_lab.out;

import com.y_lab.y_lab.entity.AuditEntity;

import java.io.IOException;
import java.util.List;

public interface Exporter {
    public void export(List<AuditEntity> entities) throws IOException;
}
