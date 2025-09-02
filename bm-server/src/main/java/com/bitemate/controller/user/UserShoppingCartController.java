package com.bitemate.controller.user;

import com.bitemate.dto.ShoppingCartDTO;
import com.bitemate.entity.ShoppingCart;
import com.bitemate.result.Result;
import com.bitemate.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "User - Shopping Cart Controller")
public class UserShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * Add item to shopping cart
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "Add Item to Shopping Cart")
    public Result<String> addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("Add to shopping cart: {}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * List all item in shopping cart
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "Get Shopping Cart List")
    public Result<List<ShoppingCart>> getShoppingCartList() {
        return Result.success(shoppingCartService.getShoppingCartList());
    }

    /**
     * Clean all items in shopping cart
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation(value = "Clean Empty Shopping Cart")
    public Result<String> cleanShoppingCart() {
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * Reduce or remove items from shopping cart
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation(value = "Remove Item from Shopping Cart")
    public Result subShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("Sub shopping cart: {}", shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
