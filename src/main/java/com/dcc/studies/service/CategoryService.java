package com.dcc.studies.service;

import com.dcc.studies.dto.*;
import com.dcc.studies.exception.ResourceNotFoundException;
import com.dcc.studies.mapper.CategoryMapper;
import com.dcc.studies.model.Category;
import com.dcc.studies.model.Content;
import com.dcc.studies.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO> findAllRootCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDTOList(categories);
    }

    public CategoryDTO findCategoryById(String categoryId) {
        Category category = findCategoryRecursively(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
        return categoryMapper.toDTO(category);
    }


    public CategoryDTO createRootCategory(CategoryCreateDTO dto) {
        Category category = categoryMapper.toModel(dto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(savedCategory);
    }

    public CategoryDTO addSubcategory(String parentId, CategoryCreateDTO dto) {
        Category rootCategory = findRootCategoryContaining(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent Category", "ID", parentId));

        Category parentCategoryNode = findInChildren(rootCategory, parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent Category Node", "ID", parentId));

        Category newSubcategory = categoryMapper.toModel(dto);

        parentCategoryNode.getSubcategories().add(newSubcategory);

        categoryRepository.save(rootCategory);

        return categoryMapper.toDTO(newSubcategory);
    }

    public ContentDTO addContentToCategory(String categoryId, ContentCreateDTO dto) {

        Category rootCategory = findRootCategoryContaining(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));

        Category targetCategoryNode = findInChildren(rootCategory, categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Node", "ID", categoryId));

        Content newContent = categoryMapper.toModel(dto);
        targetCategoryNode.getContents().add(newContent);

        categoryRepository.save(rootCategory);

        return categoryMapper.toContentDTO(newContent);
    }


    public CategoryDTO updateCategory(String categoryId, CategoryUpdateDTO dto) {

        Category rootCategory = findRootCategoryContaining(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));

        Category categoryToUpdate = findInChildren(rootCategory, categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Node", "ID", categoryId));

        categoryMapper.applyUpdate(categoryToUpdate, dto);

        categoryRepository.save(rootCategory);

        return categoryMapper.toDTO(categoryToUpdate);
    }

    public ContentDTO updateContent(String categoryId, String contentId, ContentUpdateDTO dto) {

        Category rootCategory = findRootCategoryContaining(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));

        Category parentCategoryNode = findInChildren(rootCategory, categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Node", "ID", categoryId));

        Content contentToUpdate = parentCategoryNode.getContents().stream()
                .filter(c -> c.getId().equals(contentId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Content", "ID", contentId));

        categoryMapper.applyUpdate(contentToUpdate, dto);

        categoryRepository.save(rootCategory);

        return categoryMapper.toContentDTO(contentToUpdate);
    }


    public void deleteRootCategory(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", "ID", categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    public void deleteSubcategory(String parentId, String subcategoryId) {
        Category rootCategory = findRootCategoryContaining(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent Category", "ID", parentId));

        Category parentCategoryNode = findInChildren(rootCategory, parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent Category Node", "ID", parentId));

        boolean removed = parentCategoryNode.getSubcategories().removeIf(sub -> sub.getId().equals(subcategoryId));
        if (!removed) {
            throw new ResourceNotFoundException("Subcategory", "ID", subcategoryId);
        }

        categoryRepository.save(rootCategory);
    }

    public void deleteContent(String categoryId, String contentId) {
        Category rootCategory = findRootCategoryContaining(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));

        Category parentCategoryNode = findInChildren(rootCategory, categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Node", "ID", categoryId));

        boolean removed = parentCategoryNode.getContents().removeIf(c -> c.getId().equals(contentId));
        if (!removed) {
            throw new ResourceNotFoundException("Content", "ID", contentId);
        }

        categoryRepository.save(rootCategory);
    }


    private Optional<Category> findCategoryRecursively(String categoryId) {
        Optional<Category> rootCategory = categoryRepository.findById(categoryId);
        if (rootCategory.isPresent()) {
            return rootCategory;
        }

        return categoryRepository.findAll().stream()
                .map(cat -> findInChildren(cat, categoryId))
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(optional -> optional);
    }

    private Optional<Category> findRootCategoryContaining(String nodeId) {

        if (categoryRepository.existsById(nodeId)) {
            return categoryRepository.findById(nodeId);
        }
        return categoryRepository.findAll().stream()
                .filter(root -> findInChildren(root, nodeId).isPresent())
                .findFirst();
    }


    private Optional<Category> findInChildren(Category parent, String categoryId) {
        if (parent.getId().equals(categoryId)) {
            return Optional.of(parent);
        }
        return parent.getSubcategories().stream()
                .map(child -> findInChildren(child, categoryId))
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(optional -> optional);
    }

}