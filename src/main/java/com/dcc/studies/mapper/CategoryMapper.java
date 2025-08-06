package com.dcc.studies.mapper;

import com.dcc.studies.dto.*;
import com.dcc.studies.model.Category;
import com.dcc.studies.model.Content;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSubcategories(toDTOList(category.getSubcategories()));
        dto.setContents(
                category.getContents() != null ?
                        category.getContents().stream().map(this::toContentDTO).collect(Collectors.toList()) :
                        Collections.emptyList()
        );
        return dto;
    }

    public List<CategoryDTO> toDTOList(List<Category> categories) {
        if (categories == null) return Collections.emptyList();
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ContentDTO toContentDTO(Content content) {
        if (content == null) return null;
        ContentDTO dto = new ContentDTO();
        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setBody(content.getBody());
        return dto;
    }

    public Category toModel(CategoryCreateDTO dto) {
        if (dto == null) return null;
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public Content toModel(ContentCreateDTO dto) {
        if (dto == null) return null;
        Content content = new Content();
        content.setTitle(dto.getTitle());
        content.setBody(dto.getBody());
        return content;
    }

    public void applyUpdate(Category category, CategoryUpdateDTO dto) {
        if (dto == null || category == null) return;
        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
    }

    public void applyUpdate(Content content, ContentUpdateDTO dto) {
        if (dto == null || content == null) return;
        if (dto.getTitle() != null) {
            content.setTitle(dto.getTitle());
        }
        if (dto.getBody() != null) {
            content.setBody(dto.getBody());
        }
    }
}