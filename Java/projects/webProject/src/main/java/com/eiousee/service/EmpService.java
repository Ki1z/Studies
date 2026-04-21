package com.eiousee.service;

import com.eiousee.pojo.EmpQueryParam;
import com.eiousee.pojo.Employee;
import com.eiousee.pojo.PageResult;

import java.util.List;

public interface EmpService {

    PageResult<Employee> getEmpList(EmpQueryParam params);

    Integer addEmp(Employee employee);

    void deleteEmpByIds(Integer[] ids);

    Employee getEmpById(Integer id);

    void updateEmp(Employee employee);

    List<String> getEmpNames();

    Integer getEmpCountByDeptId(Integer deptId);
}
