package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.exception.*;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.DishService;
import com.sky.service.SetMealService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sky.constant.MessageConstant;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMalService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void add(ShoppingCartDTO shoppingCartDTO) {
        Long dishId = shoppingCartDTO.getDishId();
        Long setMealId = shoppingCartDTO.getSetmealId();
        String dishFlavor = shoppingCartDTO.getDishFlavor();

        if (dishId == null && setMealId == null && dishFlavor == null)
            throw new FormValueIsNullException(MessageConstant.SHOPPING_CART_FORM_IS_NULL);
        if (dishId != null && setMealId != null)
            throw new ShoppingCartFormCollisionException(MessageConstant.SHOPPING_CART_FORM_IS_COLLISION);

        Long userId = BaseContext.getCurrentId();
        if (userId == null)
            throw new UserRecognitionFailedException(MessageConstant.CANNOT_RECOGNIZE_USER);

        ShoppingCart cart = ShoppingCart
                .builder()
                .userId(userId)
                .dishId(dishId)
                .setmealId(setMealId)
                .dishFlavor(dishFlavor)
                .build();

        // 判断该菜品是否在购物车中
        cart = shoppingCartMapper.getByIdAndFlavor(cart);
        if (cart != null) {
            // 如果在当前购物车中，则添加数量
            cart.setNumber(cart.getNumber() + 1);
            Integer count = shoppingCartMapper.update(cart);
            if (count == 0)
                throw new ShoppingCartUpdateFailedException(MessageConstant.SHOPPING_CART_UPDATE_FAILED);
        } else {
            // 如果不在当前购物车中，则添加购物车
            // 判断是否为菜品
            if (dishId != null) {
                // 如果为菜品
                DishVO dish = dishService.getById(dishId);
                cart = ShoppingCart.builder()
                        .userId(userId)
                        .name(dish.getName())
                        .image(dish.getImage())
                        .dishId(dishId)
                        .dishFlavor(dishFlavor)
                        .number(1)
                        .amount(dish.getPrice())
                        .createTime(LocalDateTime.now())
                        .build();
                Integer count = shoppingCartMapper.add(cart);
                if (count == 0)
                    throw new ShoppingCartAddFailedException(MessageConstant.SHOPPING_CART_ADD_FAILED);
            } else {
                // 如果为套餐
                SetmealVO setMeal = setMalService.getByIdWithDish(setMealId);
                cart = ShoppingCart.builder()
                        .userId(userId)
                        .name(setMeal.getName())
                        .image(setMeal.getImage())
                        .setmealId(setMealId)
                        .number(1)
                        .amount(setMeal.getPrice())
                        .createTime(LocalDateTime.now())
                        .build();
                Integer count = shoppingCartMapper.add(cart);
                if (count == 0)
                    throw new ShoppingCartAddFailedException(MessageConstant.SHOPPING_CART_ADD_FAILED);
            }
        }
    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null)
            throw new UserRecognitionFailedException(MessageConstant.CANNOT_RECOGNIZE_USER);

        return shoppingCartMapper.getByUserId(userId);
    }

    /**
     * 删除一个购物车中的数据
     * @param shoppingCartDTO
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        Long dishId = shoppingCartDTO.getDishId();
        Long setMealId = shoppingCartDTO.getSetmealId();
        String dishFlavor = shoppingCartDTO.getDishFlavor();

        if (dishId == null && setMealId == null && dishFlavor == null)
            throw new FormValueIsNullException(MessageConstant.SHOPPING_CART_FORM_IS_NULL);
        if (dishId != null && setMealId != null)
            throw new ShoppingCartFormCollisionException(MessageConstant.SHOPPING_CART_FORM_IS_COLLISION);

        Long userId = BaseContext.getCurrentId();
        if (userId == null)
            throw new UserRecognitionFailedException(MessageConstant.CANNOT_RECOGNIZE_USER);

        ShoppingCart cart = ShoppingCart
                .builder()
                .userId(userId)
                .dishId(dishId)
                .setmealId(setMealId)
                .dishFlavor(dishFlavor)
                .build();

        cart = shoppingCartMapper.getByIdAndFlavor(cart);

        if (cart == null || cart.getId() == null)
            throw new ShoppingCartNotFoundException(MessageConstant.SHOPPING_CART_NOT_FOUND);

        // 如果只有一个商品，则删除
        if (cart.getNumber() == 1) {
            Integer count = shoppingCartMapper.sub(cart);
            if (count == 0)
                throw new ShoppingCartUpdateFailedException(MessageConstant.SHOPPING_CART_UPDATE_FAILED);
            return;
        }
        // 如果有多个商品，则减1
        cart.setNumber(cart.getNumber() - 1);
        Integer count = shoppingCartMapper.update(cart);
        if (count == 0)
            throw new ShoppingCartUpdateFailedException(MessageConstant.SHOPPING_CART_UPDATE_FAILED);
    }

    /**
     * 清空购物车
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null)
            throw new UserRecognitionFailedException(MessageConstant.CANNOT_RECOGNIZE_USER);

        Integer count = shoppingCartMapper.clean(userId);
    }

    /**
     * 根据用户id查询购物车数据
     * @param userId
     * @return
     */
    @Override
    public List<ShoppingCart> getByUserId(Long userId) {
        return shoppingCartMapper.getByUserId(userId);
    }
}
