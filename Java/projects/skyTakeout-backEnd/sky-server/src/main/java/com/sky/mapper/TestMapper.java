package com.sky.mapper;

import com.sky.entity.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {
    /**
     * 插入数据
     * @param test
     */
    void insertTest(Test test);
}
