package com.eiousee.service.impl;

import com.eiousee.pojo.ClassListQueryParam;
import com.eiousee.pojo.ClassPojo;
import com.eiousee.pojo.PageResult;
import com.eiousee.pojo.ClassQueryParam;
import com.eiousee.service.ClassService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import com.eiousee.mapper.ClassMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    private final ClassMapper classMapper;

    public ClassServiceImpl(ClassMapper classMapper) {
        this.classMapper = classMapper;
    }

    @Override
    public PageResult<ClassPojo> list(ClassQueryParam queryParam) {
        Page<ClassPojo> page = PageHelper.startPage(queryParam.getPage(), queryParam.getPageSize());
        List<ClassPojo> list = classMapper.list(queryParam);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public Integer deleteClassById(Integer id) {
        return classMapper.deleteClassById(id);
    }

    @Override
    public List<String> getClassStatus() {
        return classMapper.getClassStatus();
    }

    @Override
    public Integer addClass(ClassPojo classPojo) {
        classPojo.setUpdateTime(LocalDateTime.now());
        return classMapper.addClass(classPojo);
    }

    @Override
    public ClassPojo getClassById(Integer id) {
        return classMapper.getClassById(id);
    }

    @Override
    public Integer updateClass(ClassPojo classPojo) {
        classPojo.setUpdateTime(LocalDateTime.now());
        return classMapper.updateClass(classPojo);
    }

    @Override
    public List<ClassPojo> listAll(ClassListQueryParam queryParam) {
        return classMapper.listAll(queryParam);
    }
}
