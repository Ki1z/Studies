package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.*;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private DishService dishService;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        Page<Category> page = PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    @Override
    @CacheEvict(cacheNames = "categoryCache", key = "'list'")
    public void update(CategoryDTO categoryDTO) {
        Long id = categoryDTO.getId();
        String name = categoryDTO.getName();
        Integer sort = categoryDTO.getSort();

        // 参数校验
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_CATEGORY);
        if (name == null || name.length() > 20 || name.length() < 2)
            throw new CategoryNameIllegalException(MessageConstant.CATEGORY_NAME_LENGTH_WRONG);
        Category category = categoryMapper.getByName(name);
        if (category != null && !category.getId().equals(id))
            throw new CategoryNameIllegalException(MessageConstant.CATEGORY_NAME_EXISTS);
        if (sort == null || sort < 0)
            throw new CategorySortIllegalException(MessageConstant.CATEGORY_SORT_ILLEGAL);

        category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        Integer result = categoryMapper.update(category);
        if (result != 1)
            throw new CategoryUpdateFailedException(MessageConstant.CATEGORY_UPDATE_FAILED);
    }

    /**
     * 修改分类状态
     * @param status
     * @param id
     */
    @Override
    @CacheEvict(cacheNames = "categoryCache", key = "'list'")
    public void changeStatus(Integer status, Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_CATEGORY);
        if (status == null || !status.equals(0) && !status.equals(1))
            throw new CategoryStatusIllegalException(MessageConstant.CATEGORY_STATUS_ILLEGAL);
        Integer count = categoryMapper.changeStatus(status, id);
        if (count != 1)
            throw new CategoryUpdateFailedException(MessageConstant.CATEGORY_STATUS_UPDATE_FAILED);
    }

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    @CacheEvict(cacheNames = "categoryCache", key = "'list'")
    public void add(CategoryDTO categoryDTO) {
        String name = categoryDTO.getName();
        Integer sort = categoryDTO.getSort();
        Integer type = categoryDTO.getType();

        // 参数校验
        if (name == null || name.length() > 20 || name.length() < 2)
            throw new CategoryNameIllegalException(MessageConstant.CATEGORY_NAME_LENGTH_WRONG);
        Category category = categoryMapper.getByName(name);
        if (category != null)
            throw new CategoryNameIllegalException(MessageConstant.CATEGORY_NAME_EXISTS);
        if (sort == null || sort < 0)
            throw new CategorySortIllegalException(MessageConstant.CATEGORY_SORT_ILLEGAL);
        if (type == null || !type.equals(1) && !type.equals(2))
            throw new CategoryTypeIllegalException(MessageConstant.CATEGORY_TYPE_ILLEGAL);

        category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());
//        category.setCreateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
        category.setStatus(StatusConstant.ENABLE);

        Integer result = categoryMapper.add(category);
        if (result != 1)
            throw new CategoryInsertFailedException(MessageConstant.CATEGORY_INSERT_FAILED);
    }

    /**
     * 删除分类
     * @param id
     */
    @Override
    @CacheEvict(cacheNames = "categoryCache", key = "'list'")
    public void delete(Long id) {
       if (id == null)
           throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_CATEGORY);

       // 判断当前分类是否关联了套餐和菜品
        if (setMealService.getCountByCategoryId(id) > 0)
            throw new CategoryIsAssociatedException(MessageConstant.CATEGORY_IS_ASSOCIATED);
        if (dishService.getCountByCategoryId(id) > 0)
            throw new CategoryIsAssociatedException(MessageConstant.CATEGORY_IS_ASSOCIATED);

        Integer count = categoryMapper.delete(id);
       if (count != 1)
           throw new CategoryDeleteFailedException(MessageConstant.CATEGORY_DELETE_FAILED);
    }

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    @Override
    @Cacheable(cacheNames = "categoryCache", key = "'list'")
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
