package com.bitemate.mapper;

import com.bitemate.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * Conditional query
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> getList(ShoppingCart shoppingCart);

    /**
     * Update Num of item in the shopping cart
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateItemNumById(ShoppingCart shoppingCart);

    /**
     * Insert new item in the shopping cart
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            " values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insertShoppingCart(ShoppingCart shoppingCart);

    /**
     * Clean all items in shopping cart
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * Reduce or remove items from shopping cart
     * @param id
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);

    /**
     * Insert items into shopping cart
     * @param cartList
     */
    void insertBatch(List<ShoppingCart> cartList);
}
