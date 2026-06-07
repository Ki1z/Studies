package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 更新购物车数据
     * @param cart
     */
    Integer update(ShoppingCart cart);

    /**
     * 根据菜品id、套餐id、口味、用户id查询购物车数据
     * @param cart
     * @return
     */
    ShoppingCart getByIdAndFlavor(ShoppingCart cart);

    /**
     * 添加购物车数据
     * @param cart
     * @return
     */
    Integer add(ShoppingCart cart);

    /**
     * 根据用户id查询购物车数据
     * @param userId
     * @return
     */
    List<ShoppingCart> getByUserId(Long userId);

    /**
     * 删除一个购物车数据
     * @param cart
     * @return
     */
    Integer sub(ShoppingCart cart);

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    Integer clean(Long userId);
}
