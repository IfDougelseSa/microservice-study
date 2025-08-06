package com.dcc.studies.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "categories")
public class Category {

    @Id
    private String id = UUID.randomUUID().toString();

    private String name;
    private AuditMetadata audit = new AuditMetadata();
    private List<Category> subcategories = new ArrayList<>();
    private List<Content> contents = new ArrayList<>();
}