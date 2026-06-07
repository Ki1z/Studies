package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user/setmeal")
@Api(tags = "C端套餐接口")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    public Result<List<SetmealVO>> list(Long categoryId) {
        log.info("根据分类id查询套餐：{}", categoryId);
        List<SetmealVO> list = setMealService.getByCategoryId(categoryId);
        // 删除停售的套餐
        list.removeIf(setmealVO -> Objects.equals(setmealVO.getStatus(), StatusConstant.DISABLE));
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品列表")
    public Result<List<SetmealDish>> getSetmealDishList(@PathVariable Long id) {
        log.info("根据套餐id查询包含的菜品列表：{}", id);
        List<SetmealDish> list = setMealService.getSetmealDishList(id);
        return Result.success(list);
    }
}
