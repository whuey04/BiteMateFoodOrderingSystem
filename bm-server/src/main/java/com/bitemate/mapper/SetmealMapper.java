package com.bitemate.mapper;

import com.bitemate.annotation.AutoFill;
import com.bitemate.dto.SetmealPageQueryDTO;
import com.bitemate.entity.Setmeal;
import com.bitemate.enumeration.OperationType;
import com.bitemate.vo.DishItemVO;
import com.bitemate.vo.SetmealVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {
    /**
     * Count the num of dish by using Category id
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * Get setmeal data
     * @param dto
     * @return
     */
    Page<SetmealVO> pageQuerySetmeal(SetmealPageQueryDTO dto);

    /**
     * Insert Dish
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    void insertSetmeal(Setmeal setmeal);

    /**
     * Get setmeal by id
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getSetmealById(Long id);

    /**
     * Update
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateSetmeal(Setmeal setmeal);

    /**
     * Delete set meal
     * @param id
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long id);


    //User side

    /**
     * List all set meal in user side
     * @param setmeal
     * @return
     */
    List<Setmeal> listSetmeal(Setmeal setmeal);

    /**
     * Query all dish by using set meal id
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * Count the num of setmeal based on conditions
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    List<Setmeal> getSetmealByIds(List<Long> ids);
}
