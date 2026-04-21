package com.eiousee.service;

import com.eiousee.pojo.Dept;
import java.util.List;

public interface DeptService {
    List<Dept> getAllDepts();

    Integer deleteDeptById(Integer id);

    int addDept(Dept dept);

    Dept getDeptById(Integer id);

    Integer updateDept(Dept dept);

    Integer getDeptIdByName(String deptName);
}
