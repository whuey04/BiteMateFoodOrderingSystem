package com.bitemate.service;

import com.bitemate.dto.CategoryDTO;
import com.bitemate.dto.CategoryPageQueryDTO;
import com.bitemate.entity.Category;
import com.bitemate.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * Page Query of Category
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQueryCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * Add New Category
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * Update Category
     * @param categoryDTO
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * Update Category
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);

    /**
     * List the category by TYPE
     * @param type
     * @return
     */
    List<Category> listCategory(Integer type);

    /**
     * Delete Category By id
     * @param id
     */
    void deleteCategoryById(Long id);
}
