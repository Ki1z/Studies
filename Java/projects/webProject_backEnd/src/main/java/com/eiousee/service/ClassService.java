package com.eiousee.service;

import com.eiousee.pojo.ClassListQueryParam;
import com.eiousee.pojo.ClassPojo;
import com.eiousee.pojo.PageResult;
import com.eiousee.pojo.ClassQueryParam;

import java.util.List;

public interface ClassService {
    PageResult<ClassPojo> list(ClassQueryParam queryParam);

    Integer deleteClassById(Integer id);

    List<String> getClassStatus();

    Integer addClass(ClassPojo classPojo);

    ClassPojo getClassById(Integer id);

    Integer updateClass(ClassPojo classPojo);

    List<ClassPojo> listAll(ClassListQueryParam queryParam);
}
