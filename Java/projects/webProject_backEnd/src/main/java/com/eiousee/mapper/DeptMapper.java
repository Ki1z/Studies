package com.eiousee.mapper;

import com.eiousee.pojo.Dept;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface DeptMapper {
    @Select("SELECT id, name, create_time, update_time FROM `dept` ORDER BY update_time DESC")
    List<Dept> getAllDepts();

    @Delete("DELETE FROM `dept` WHERE id = #{id}")
    Integer deleteDeptById(Integer id);

    @Insert("INSERT INTO `dept` (name, create_time, update_time) VALUES (#{name}, #{createTime}, #{updateTime})")
    Integer addDept(Dept dept);

    @Select("SELECT id, name, create_time, update_time FROM `dept` WHERE id = #{id}")
    Dept getDeptById(Integer id);

    @Update("UPDATE `dept` SET name = #{name}, update_time = #{updateTime} WHERE id = #{id}")
    Integer updateDept(Dept dept);

    @Select("SELECT id FROM `dept` WHERE name = #{name}")
    Integer getDeptIdByName(String name);
}
