package com.eiousee.service.impl;

import com.eiousee.mapper.EmpExpMapper;
import com.eiousee.mapper.EmpMapper;
import com.eiousee.pojo.EmpQueryParam;
import com.eiousee.pojo.Employee;
import com.eiousee.pojo.PageResult;
import com.eiousee.service.DeptService;
import com.eiousee.service.EmpService;
import com.eiousee.service.JobService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    private final EmpMapper empMapper;
    private final EmpExpMapper empExpMapper;
    private final DeptService deptService;
    private final JobService jobService;

    public EmpServiceImpl(EmpMapper empMapper, EmpExpMapper empExpMapper, DeptService deptService, JobService jobService) {
        this.empMapper = empMapper;
        this.empExpMapper = empExpMapper;
        this.deptService = deptService;
        this.jobService = jobService;
    }

    /**
     * 获取员工列表
     * @param params 查询参数
     * @return 员工列表
     */
    @Override
    public PageResult<Employee> getEmpList(EmpQueryParam params) {
        Page<Employee> page = PageHelper.startPage(params.getPage(), params.getPageSize());
        List<Employee> list = empMapper.getEmpList(params);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 添加员工
     * @param employee 员工信息
     * @return 添加结果
     */
    @Override
    @Transactional(rollbackForClassName = "Exception")
    public Integer addEmp(Employee employee) {
        // 获取当前时间
        employee.setUpdateTime(LocalDateTime.now());
        // 获取员工部门id、职位id
        Integer deptId = deptService.getDeptIdByName(employee.getDeptName());
        Integer jobId = jobService.getJobIdByName(employee.getJobName());
        // 插入员工信息
        empMapper.addEmp(employee, deptId, jobId);
        if (employee.getEmpExpList() != null && !employee.getEmpExpList().isEmpty()) {
            employee.getEmpExpList().forEach(empExp -> empExp.setId(employee.getId()));
            return empExpMapper.addEmpExp(employee.getEmpExpList());
        } else return 1;
    }

    /**
     * 删除员工
     * @param ids 员工id列表
     */
    @Override
    @Transactional(rollbackForClassName = "Exception")
    public void deleteEmpByIds(Integer[] ids) {
        empMapper.deleteEmpByIds(ids);
        empExpMapper.deleteEmpExpByIds(ids);
    }

    /**
     * 根据id查询员工
     * @param id 员工id
     * @return 员工信息
     */
    @Override
    public Employee getEmpById(Integer id) {
        return empMapper.getEmpAndExpById(id);
    }

    /**
     * 更新员工信息
     * @param employee 员工信息
     */
    @Override
    @Transactional(rollbackForClassName = "Exception")
    public void updateEmp(Employee employee) {
        employee.setUpdateTime(LocalDateTime.now());
        empMapper.updateEmp(employee);
        empExpMapper.deleteEmpExpByIds(new Integer[]{employee.getId()});

        if (employee.getEmpExpList() != null && !employee.getEmpExpList().isEmpty()) {
            empExpMapper.addEmpExp(employee.getEmpExpList());
        }
    }

    /**
     * 获取所有员工名称
     * @return 员工名称列表
     */
    @Override
    public List<String> getEmpNames() {
        return empMapper.getEmpNames();
    }

    /**
     * 根据部门id返回员工数量
     * @return 员工数量
     */
    @Override
    public Integer getEmpCountByDeptId(Integer deptId) {
        return empMapper.getEmpCountByDeptId(deptId);
    }
}
