package com.community.marketplace.controller;

import com.community.marketplace.common.ApiResponse;
import com.community.marketplace.entity.Category;
import com.community.marketplace.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<Category>> listCategories() {
        return ApiResponse.success(categoryService.listCategories());
    }

    @PostMapping
    public ApiResponse<Category> createCategory(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        Integer sortOrder = body.get("sortOrder") != null
                ? ((Number) body.get("sortOrder")).intValue() : 0;
        return ApiResponse.success(categoryService.createCategory(name, sortOrder));
    }
}
