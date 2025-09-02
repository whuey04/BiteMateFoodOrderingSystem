package com.bitemate.controller.user;

import com.bitemate.entity.Category;
import com.bitemate.result.Result;
import com.bitemate.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/category")
@Slf4j
@Api(tags = "User - Category Controller")
public class UserCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * List the category by TYPE
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "Get List of Category")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.listCategory(type);
        return Result.success(list);
    }

}
