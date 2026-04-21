package com.eiousee.controller;

import com.eiousee.pojo.EmpQueryParam;
import com.eiousee.pojo.Employee;
import com.eiousee.pojo.Result;
import com.eiousee.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    private final EmpService empService;

    public EmpController(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping
    public Result getEmpList(EmpQueryParam params) {
        log.info("查询员工列表，参数：{}", params);
        return Result.success(empService.getEmpList(params));
    }

    @PostMapping
    public Result addEmp(@RequestBody Employee employee) {
        log.info("添加员工，参数：{}", employee);
        return empService.addEmp(employee) > 0 ? Result.success() : Result.error("添加失败");
    }

    @DeleteMapping
    public Result deleteEmpByIds(Integer[] ids) {
        log.info("删除员工，参数：{}", Arrays.toString(ids));
        empService.deleteEmpByIds(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getEmpById(@PathVariable Integer id) {
        log.info("查询员工，参数：{}", id);
        return Result.success(empService.getEmpById(id));
    }

    @PutMapping
    public Result updateEmp(@RequestBody Employee employee) {
        log.info("更新员工，参数：{}", employee);
        empService.updateEmp(employee);
        return Result.success();
    }

    @GetMapping("/names")
    public Result getEmpNames() {
        log.info("查询员工名称列表");
        return Result.success(empService.getEmpNames());
    }
}
