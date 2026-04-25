package com.eiousee.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobMapper {
    @Select("SELECT id FROM `job` WHERE title = #{name}")
    Integer getJobIdByName(String name);

    @Select("SELECT title FROM `job`")
    List<String> getJobList();
}
