package com.bitemate.mapper;

import com.bitemate.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * Insert flavor data in batch
     * @param flavorList
     */
    void insertBatch(List<DishFlavor> flavorList);

    /**
     * Get list of dish flavor by dish id
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getFlavorByDishId(Long id);

    /**
     * Delete flavor by dish id
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id = #{id};")
    void deleteByDishId(Long id);
}
