package com.bitemate.mapper;

import com.bitemate.annotation.AutoFill;
import com.bitemate.dto.CategoryPageQueryDTO;
import com.bitemate.entity.Category;
import com.bitemate.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * Page Query
     *
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQueryCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * Insert category to database
     *
     * @param category
     */
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insertCategory(Category category);

    /**
     * Update Category
     *
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateCategory(Category category);

    /**
     * List the category by TYPE
     *
     * @param type
     * @return
     */
    List<Category> listCategory(Integer type);

    /**
     * Delete category by id
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void deleteCategoryById(Long id);
}
