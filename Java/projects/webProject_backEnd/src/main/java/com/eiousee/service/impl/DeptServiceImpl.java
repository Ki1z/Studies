package com.eiousee.service.impl;

import com.eiousee.mapper.DeptMapper;
import com.eiousee.pojo.Dept;
import com.eiousee.service.DeptService;
import com.eiousee.service.EmpService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    private final DeptMapper deptMapper;
    private final EmpService empService;

    public DeptServiceImpl(DeptMapper deptMapper, @Lazy EmpService empService) {
        this.deptMapper = deptMapper;
        this.empService = empService;
    }

    @Override
    public List<Dept> getAllDepts() {
        return deptMapper.getAllDepts();
    }

    @Override
    public Integer deleteDeptById(Integer id) {
        // 判断该部门下是否有员工
        if (empService.getEmpCountByDeptId(id) > 0) {
            return -1;
        }
        return deptMapper.deleteDeptById(id);
    }

    @Override
    public int addDept(Dept dept) {
        LocalDateTime now = LocalDateTime.now();
        dept.setCreateTime(now);
        dept.setUpdateTime(now);
        return deptMapper.addDept(dept);
    }

    @Override
    public Dept getDeptById(Integer id) {
        return deptMapper.getDeptById(id);
    }

    @Override
    public Integer getDeptIdByName(String deptName) {
        return deptMapper.getDeptIdByName(deptName);
    }

    @Override
    public Integer updateDept(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        return deptMapper.updateDept(dept);
    }
}
