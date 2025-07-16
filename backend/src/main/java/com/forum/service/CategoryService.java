package com.forum.service;

import com.forum.dto.CategoryDTO;
import com.forum.entity.Category;
import com.forum.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // 检查分类名是否已存在
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new RuntimeException("分类名已存在");
        }
        
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setColor(categoryDTO.getColor());
        
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDTO(savedCategory);
    }
    
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAllOrderByName()
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }
    
    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(CategoryDTO::new).orElse(null);
    }
    
    public CategoryDTO getCategoryByName(String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        return category.map(CategoryDTO::new).orElse(null);
    }
    
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            
            // 检查新名称是否已存在（除了当前分类）
            if (!category.getName().equals(categoryDTO.getName()) && 
                categoryRepository.existsByName(categoryDTO.getName())) {
                throw new RuntimeException("分类名已存在");
            }
            
            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());
            category.setColor(categoryDTO.getColor());
            
            Category updatedCategory = categoryRepository.save(category);
            return new CategoryDTO(updatedCategory);
        }
        return null;
    }
    
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 