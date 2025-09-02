package com.bitemate.controller.admin;

import com.bitemate.dto.CategoryDTO;
import com.bitemate.dto.CategoryPageQueryDTO;
import com.bitemate.entity.Category;
import com.bitemate.result.PageResult;
import com.bitemate.result.Result;
import com.bitemate.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "Admin - Category Controller")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Page query for all category data
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "Page Query for Category")
    public Result<PageResult> queryCategoryByPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("Category Page Query:{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQueryCategory(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Add New Category
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "Add New Category")
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("Add New Category:{}", categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * Update Category
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "Update Category")
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("Update Category:{}", categoryDTO);
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * Update Category Status
     *
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "Update Status of Category")
    public Result updateCategoryStatus(@PathVariable("status") Integer status, Long id) {
        log.info("Update Category = status:{},id:{}", status, id);
        categoryService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * List the category by TYPE
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "List Category By Type")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.listCategory(type);
        return Result.success(list);
    }

    /**
     * Delete Category by id
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "Delete Category")
    public Result<String> deleteCategory(Long id) {
        log.info("Delete Category:{}", id);
        categoryService.deleteCategoryById(id);
        return Result.success();
    }
}
