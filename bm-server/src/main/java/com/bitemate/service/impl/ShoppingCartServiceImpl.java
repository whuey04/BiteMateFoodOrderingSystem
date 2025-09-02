package com.bitemate.service.impl;

import com.bitemate.context.BaseContext;
import com.bitemate.dto.ShoppingCartDTO;
import com.bitemate.entity.Dish;
import com.bitemate.entity.Setmeal;
import com.bitemate.entity.ShoppingCart;
import com.bitemate.mapper.DishMapper;
import com.bitemate.mapper.SetmealMapper;
import com.bitemate.mapper.ShoppingCartMapper;
import com.bitemate.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * Add item to shopping cart
     *
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        //Check shopping cart by own user id
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //Check if the current item is in the shopping cart
        List<ShoppingCart> cartList = shoppingCartMapper.getList(shoppingCart);

        if (cartList != null && cartList.size() == 1) {
            shoppingCart = cartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateItemNumById(shoppingCart);
        }
        else {
            //No, insert a new data

            //Determine whether the current item added to the shopping cart is a dish or a set meal.
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                Dish dish = dishMapper.getDishById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }
            else {
                Setmeal setmeal = setmealMapper.getSetmealById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insertShoppingCart(shoppingCart);
        }
    }

    /**
     * List all item in shopping cart
     *
     * @return
     */
    public List<ShoppingCart> getShoppingCartList() {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();

        return shoppingCartMapper.getList(shoppingCart);
    }

    /**
     * Clean all items in shopping cart
     */
    public void cleanShoppingCart() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    /**
     * Reduce or remove items from shopping cart
     *
     * @param shoppingCartDTO
     */
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //Get the list of shopping cart
        List<ShoppingCart> cartList = shoppingCartMapper.getList(shoppingCart);

        if (cartList != null && cartList.size() == 1) {
            shoppingCart = cartList.get(0);

            Integer number = shoppingCart.getNumber();
            if (number ==  1){
                //Delete the item from shopping cart
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }
            else{
                //Minus the num of item in shopping cart
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateItemNumById(shoppingCart);
            }
        }
    }
}
