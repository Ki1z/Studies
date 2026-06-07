package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.*;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.service.SetMealService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealService setMealService;

    /**
     * 根据分类id查询菜品数量
     * @param id
     * @return
     */
    @Override
    public Integer getCountByCategoryId(Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_CATEGORY);

        return dishMapper.getCountByCategoryId(id);
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        Page<DishVO> page = PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询菜品和对应的口味数据
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_DISH);
        DishVO dish = dishMapper.getById(id);
        if (dish == null)
            throw new NoSuchDishException(MessageConstant.NO_SUCH_DISH);
        return dish;
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    @Cacheable(cacheNames = "dishCache", key = "'categoryId:' + #categoryId")
    public List<DishVO> list(Long categoryId) {
        if (categoryId == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_CATEGORY);
        return dishMapper.getByCategoryId(categoryId);
    }

    /**
     * 菜品状态更改
     * @param status
     * @param id
     * @return
     */
    @Override
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public void changeStatus(Integer status, Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_DISH);
        if (status == null)
            throw new FormValueIsNullException(MessageConstant.STATUS_IS_NOT_DEFINED);

        Integer count = dishMapper.changeStatus(status, id);
        if (count == 0)
            throw new DishStatusChangeFailedException(MessageConstant.DISH_STATUS_CHANGE_FAILED);
    }

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    @CacheEvict(cacheNames = "dishCache", key = "'categoryId:' + #dishDTO.categoryId")
    public void add(DishDTO dishDTO) {
        String name = dishDTO.getName();
        Long categoryId = dishDTO.getCategoryId();
        BigDecimal price = dishDTO.getPrice();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        String image = dishDTO.getImage();

        // 数据校验
        // name
        if (name == null || name.length() > 20 || name.length() < 2)
            throw new DishNameIllegalException(MessageConstant.DISH_NAME_ILLEGAL);
        Integer count = dishMapper.getCountByName(name);
        if (count > 0)
            throw new DishIsExistedException(MessageConstant.DISH_IS_EXISTED);
        // categoryId
        if (categoryId == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_CATEGORY);
        // price
        if (price == null || price.compareTo(new BigDecimal(0)) < 0)
            throw new DishPriceIllegalException(MessageConstant.DISH_PRICE_ILLEGAL);
        // image
        if (image == null || image.isEmpty())
            throw new CannotFoundDishImageException(MessageConstant.CANNOT_FOUND_DISH_IMAGE);

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 设置状态信息
        dish.setStatus(StatusConstant.ENABLE);

        // 将基本信息插入dish表
        count = dishMapper.add(dish);
        if (count != 1)
            throw new CannotAddDishException(MessageConstant.CANNOT_ADD_DISH);

        // 插入口味
        if (flavors != null && !flavors.isEmpty()) {
            Long dishId = dish.getId();
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            count = dishMapper.addFlavors(flavors);
            if (!Objects.equals(count, flavors.size()))
                throw new CannotAddDishException(MessageConstant.CANNOT_ADD_DISH_FLAVOR);
        }
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public void delete(Long[] ids) {
        if (ids == null || ids.length == 0)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_DISH);

        // 关联校验
        for (Long id : ids) {
            DishVO dish = dishMapper.getById(id);
            if (dish == null)
                throw new DishDeleteFailedException(MessageConstant.NO_SUCH_DISH);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE))
                throw new DishDeleteFailedException(MessageConstant.DISH_IS_ON_SALE);
            if (setMealService.getCountByDishId(id) > 0)
                throw new DishDeleteFailedException(MessageConstant.DISH_IS_RELATED_TO_SET_MEAL);
        }

        // 从菜品表删除
        Integer count = dishMapper.delete(ids);
        if (count == 0)
            throw new DishDeleteFailedException(MessageConstant.DISH_DELETE_FAILED);
        // 从口味表删除
        dishMapper.deleteFlavors(ids);
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    @CacheEvict(cacheNames = "dishCache", key = "'categoryId:' + #dishDTO.categoryId")
    public void update(DishDTO dishDTO) {
        Long id = dishDTO.getId();
        String name = dishDTO.getName();
        Long categoryId = dishDTO.getCategoryId();
        BigDecimal price = dishDTO.getPrice();
        String image = dishDTO.getImage();
        List<DishFlavor> flavors = dishDTO.getFlavors();

        // 数据校验
        // id
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_DISH);
        // name
        if (name == null || name.length() > 20 || name.length() < 2)
            throw new DishNameIllegalException(MessageConstant.DISH_NAME_ILLEGAL);
        // name重复
        Dish dish = dishMapper.getByName(name);
        if (dish != null && !Objects.equals(dish.getId(), id))
            throw new DishIsExistedException(MessageConstant.DISH_IS_EXISTED);
        // categoryId
        if (categoryId == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_CATEGORY);
        // price
        if (price == null || price.compareTo(new BigDecimal(0)) < 0)
            throw new DishPriceIllegalException(MessageConstant.DISH_PRICE_ILLEGAL);
        // image
        if (image == null || image.isEmpty())
            throw new CannotFoundDishImageException(MessageConstant.CANNOT_FOUND_DISH_IMAGE);

        dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 更新dish表中的信息
        Integer count = dishMapper.update(dish);
        if (count != 1)
            throw new DishUpdateFailedException(MessageConstant.DISH_UPDATE_FAILED);

        // 更新口味表
        dishMapper.deleteFlavors(new Long[]{id});
        if (flavors != null && !flavors.isEmpty()) {
            Long dishId = dish.getId();
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            count = dishMapper.addFlavors(flavors);
            if (!Objects.equals(count, flavors.size()))
                throw new DishUpdateFailedException(MessageConstant.DISH_UPDATE_FAILED);
        }
    }
}
