package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     * @param id
     * @return
     */
    @Select("select count(id) from dish where category_id = #{id}")
    Integer getCountByCategoryId(Long id);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    List<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品和对应的口味数据
     * @param id
     * @return
     */
    DishVO getById(Long id);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<DishVO> getByCategoryId(Long categoryId);

    /**
     * 菜品状态更改
     * @param status
     * @param id
     * @return
     */
    @Insert("update dish set status = #{status} where id = #{id}")
    Integer changeStatus(Integer status, Long id);

    /**
     * 根据名称查询菜品数量
     * @param name
     * @return
     */
    @Select("select count(id) from dish where name = #{name}")
    Integer getCountByName(String name);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    Integer add(Dish dish);

    /**
     * 新增菜品口味数据
     * @param flavors
     * @return
     */
    Integer addFlavors(List<DishFlavor> flavors);

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    Integer delete(Long[] ids);

    /**
     * 批量删除菜品口味数据
     * @param ids
     * @return
     */
    void deleteFlavors(Long[] ids);

    /**
     * 根据名称查询菜品
     * @param name
     * @return
     */
    @Select("select * from dish where name = #{name}")
    Dish getByName(String name);

    /**
     * 修改菜品
     * @param dish
     * @return
     */
    @AutoFill(OperationType.UPDATE)
    Integer update(Dish dish);

    /**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);
}
