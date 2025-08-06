package com.dcc.studies.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryDTO {
    private String id;
    private String name;
    private List<CategoryDTO> subcategories;
    private List<ContentDTO> contents;
}