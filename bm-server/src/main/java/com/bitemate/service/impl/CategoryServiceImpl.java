package com.bitemate.service.impl;

import com.bitemate.constant.MessageConstant;
import com.bitemate.constant.StatusConstant;
import com.bitemate.context.BaseContext;
import com.bitemate.dto.CategoryDTO;
import com.bitemate.dto.CategoryPageQueryDTO;
import com.bitemate.entity.Category;
import com.bitemate.exception.DeletionNotAllowedException;
import com.bitemate.mapper.CategoryMapper;
import com.bitemate.mapper.DishMapper;
import com.bitemate.mapper.SetmealMapper;
import com.bitemate.result.PageResult;
import com.bitemate.service.CategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * Page Query of Category
     *
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQueryCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());

        Page<Category> categoryPage = categoryMapper.pageQueryCategory(categoryPageQueryDTO);

        long total = categoryPage.getTotal();
        List<Category> result = categoryPage.getResult();

        return new PageResult(total, result);
    }

    /**
     * Add New Category
     *
     * @param categoryDTO
     */
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        category.setStatus(StatusConstant.DISABLE);

//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insertCategory(category);

    }

    /**
     * Update Category
     *
     * @param categoryDTO
     */
    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.updateCategory(category);
    }

    /**
     * Update Category
     *
     * @param status
     * @param id
     */
    public void updateStatus(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
//                .updateTime(LocalDateTime.now())
//                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.updateCategory(category);
    }

    /**
     * List the category by TYPE
     *
     * @param type
     * @return
     */
    public List<Category> listCategory(Integer type){
        return categoryMapper.listCategory(type);
    }

    /**
     * Delete Category By id
     *
     * @param id
     */
    public void deleteCategoryById(Long id) {
        // Check if the category is associated with any dishes
        Integer count = dishMapper.countByCategoryId(id);
        if (count > 0) {
            // Cannot delete: category is linked to existing dishes
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // Check if the category is associated with any setmeals
        count = setmealMapper.countByCategoryId(id);
        if (count > 0) {
            // Cannot delete: category is linked to existing setmeals
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        //No error. Delete the category
        categoryMapper.deleteCategoryById(id);
    }
}
