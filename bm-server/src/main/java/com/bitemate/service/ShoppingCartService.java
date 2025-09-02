package com.bitemate.service;

import com.bitemate.dto.ShoppingCartDTO;
import com.bitemate.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /**
     * Add item to shopping cart
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * List all item in shopping cart
     * @return
     */
    List<ShoppingCart> getShoppingCartList();

    /**
     * Clean all items in shopping cart
     */
    void cleanShoppingCart();

    /**
     * Reduce or remove items from shopping cart
     * @param shoppingCartDTO
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
