package com.community.marketplace.service;

import com.community.marketplace.common.BusinessException;
import com.community.marketplace.entity.Category;
import com.community.marketplace.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> listCategories() {
        return categoryRepository.findAllByOrderBySortOrderAsc();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "分类不存在"));
    }

    @Transactional
    public Category createCategory(String name, Integer sortOrder) {
        Category category = Category.builder()
                .name(name)
                .sortOrder(sortOrder)
                .build();
        return categoryRepository.save(category);
    }
}
