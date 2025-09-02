package com.bitemate.mapper;

import com.bitemate.annotation.AutoFill;
import com.bitemate.dto.DishPageQueryDTO;
import com.bitemate.entity.Dish;
import com.bitemate.enumeration.OperationType;
import com.bitemate.vo.DishVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * Count the num of dish by using Category id
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * Page Query for dish
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQueryDish(DishPageQueryDTO dishPageQueryDTO);

    /**
     * Insert dish data into database
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insertDish(Dish dish);

    /**
     * Get dish by id
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getDishById(Long id);

    /**
     * Update dish
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateDish(Dish dish);

    /**
     * Delete dish
     * @param id
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * Search for dishes by category id
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * Get setmeal_dish by set meal id
     * @param setmealId
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getDishBySetmealId(Long setmealId);

    /**
     * Count the num of dishes based on conditions
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    List<Dish> getDishByIds(List<Long> ids);
}
