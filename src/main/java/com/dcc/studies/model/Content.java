package com.dcc.studies.model;

import lombok.Data;
import java.util.UUID;

@Data
public class Content {
    private String id = UUID.randomUUID().toString();
    private String title;
    private String body;
    private AuditMetadata audit = new AuditMetadata();
}