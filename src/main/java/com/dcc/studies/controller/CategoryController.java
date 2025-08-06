package com.dcc.studies.controller;

import com.dcc.studies.dto.*;
import com.dcc.studies.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(
        path = "/api/categories",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponseDTO<List<CategoryDTO>>> getAllRootCategories() {
        List<CategoryDTO> categoriesData = categoryService.findAllRootCategories();
        SuccessResponseDTO<List<CategoryDTO>> response = new SuccessResponseDTO<>("200", "Recursos encontrados com sucesso", categoriesData);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<SuccessResponseDTO<CategoryDTO>> getCategoryById(@PathVariable String categoryId) {
        CategoryDTO categoryData = categoryService.findCategoryById(categoryId);
        SuccessResponseDTO<CategoryDTO> response = new SuccessResponseDTO<>("200", "Recurso encontrado com sucesso", categoryData);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<CategoryDTO>> createRootCategory(@RequestBody CategoryCreateDTO dto) {
        CategoryDTO createdCategoryData = categoryService.createRootCategory(dto);
        SuccessResponseDTO<CategoryDTO> response = new SuccessResponseDTO<>("201", "Categoria criada com sucesso", createdCategoryData);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCategoryData.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping(path = "/{parentId}/subcategories", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<CategoryDTO>> addSubcategory(@PathVariable String parentId, @RequestBody CategoryCreateDTO dto) {
        CategoryDTO newSubcategoryData = categoryService.addSubcategory(parentId, dto);
        SuccessResponseDTO<CategoryDTO> response = new SuccessResponseDTO<>("201", "Subcategoria criada com sucesso", newSubcategoryData);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories/{id}").buildAndExpand(newSubcategoryData.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping(path = "/{categoryId}/contents", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<ContentDTO>> addContentToCategory(@PathVariable String categoryId, @RequestBody ContentCreateDTO dto) {
        ContentDTO newContentData = categoryService.addContentToCategory(categoryId, dto);
        SuccessResponseDTO<ContentDTO> response = new SuccessResponseDTO<>("201", "Conteúdo criado com sucesso", newContentData);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories/{id}").buildAndExpand(categoryId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(path = "/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<CategoryDTO>> updateCategory(@PathVariable String categoryId, @RequestBody CategoryUpdateDTO dto) {
        CategoryDTO updatedCategoryData = categoryService.updateCategory(categoryId, dto);
        SuccessResponseDTO<CategoryDTO> response = new SuccessResponseDTO<>("200", "Categoria atualizada com sucesso", updatedCategoryData);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{categoryId}/contents/{contentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<ContentDTO>> updateContent(@PathVariable String categoryId, @PathVariable String contentId, @RequestBody ContentUpdateDTO dto) {
        ContentDTO updatedContentData = categoryService.updateContent(categoryId, contentId, dto);
        SuccessResponseDTO<ContentDTO> response = new SuccessResponseDTO<>("200", "Conteúdo atualizado com sucesso", updatedContentData);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{categoryId}")
    public ResponseEntity<SuccessResponseDTO<Object>> deleteRootCategory(@PathVariable String categoryId) {
        categoryService.deleteRootCategory(categoryId);
        SuccessResponseDTO<Object> response = new SuccessResponseDTO<>("200", "Recurso deletado com sucesso", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{parentId}/subcategories/{subcategoryId}")
    public ResponseEntity<SuccessResponseDTO<Object>> deleteSubcategory(@PathVariable String parentId, @PathVariable String subcategoryId) {
        categoryService.deleteSubcategory(parentId, subcategoryId);
        SuccessResponseDTO<Object> response = new SuccessResponseDTO<>("200", "Subcategoria deletada com sucesso", null);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{categoryId}/contents/{contentId}")
    public ResponseEntity<SuccessResponseDTO<Object>> deleteContent(@PathVariable String categoryId, @PathVariable String contentId) {
        categoryService.deleteContent(categoryId, contentId);
        SuccessResponseDTO<Object> response = new SuccessResponseDTO<>("200", "Conteúdo deletado com sucesso", null);
        return ResponseEntity.ok(response);
    }
}