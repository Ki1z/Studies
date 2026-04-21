package com.eiousee.mapper;

import com.eiousee.pojo.ClassListQueryParam;
import com.eiousee.pojo.ClassPojo;
import com.eiousee.pojo.ClassQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassMapper {
    List<ClassPojo> list(ClassQueryParam queryParam);

    @Delete("delete from class where id = #{id}")
    Integer deleteClassById(Integer id);

    @Select("SELECT status_name FROM class_status")
    List<String> getClassStatus();

    Integer addClass(ClassPojo classPojo);

    ClassPojo getClassById(Integer id);

    Integer updateClass(ClassPojo classPojo);

    List<ClassPojo> listAll(ClassListQueryParam queryParam);
}
