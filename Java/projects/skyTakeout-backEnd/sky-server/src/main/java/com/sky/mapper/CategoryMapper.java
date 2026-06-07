package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    @Select("select id, type, name, sort, status, create_time, update_time, create_user, update_user from category where name = #{name}")
    Category getByName(String name);

    /**
     * 修改分类
     * @param category
     */
    @AutoFill(OperationType.UPDATE)
    Integer update(Category category);

    /**
     * 修改分类状态
     * @param status
     * @param id
     * @return
     */
    @Update("update category set status = #{status} where id = #{id}")
    Integer changeStatus(Integer status, Long id);

    /**
     * 新增分类
     * @param category
     * @return
     */
    @AutoFill(OperationType.INSERT)
    Integer add(Category category);

    /**
     * 删除分类
     * @param id
     * @return
     */
    @Delete("delete from category where id = #{id}")
    Integer delete(Long id);

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
