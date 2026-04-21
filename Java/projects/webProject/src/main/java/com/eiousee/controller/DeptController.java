package com.eiousee.controller;

import com.eiousee.pojo.Dept;
import com.eiousee.pojo.Result;
import com.eiousee.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {

    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @GetMapping
    public Result list() {
        log.info("查询所有部门数据");
        List<Dept> depts = deptService.getAllDepts();
        return Result.success(depts);
    }

    @DeleteMapping
    public Result delete(Integer id) {
        log.info("根据id删除部门：{}", id);
        return switch (deptService.deleteDeptById(id)) {
            case 1 -> Result.success();
            case -1 -> Result.error("部门下有员工，不能删除");
            default -> Result.error("删除失败");
        };
    }

    @PostMapping
    public Result add(@RequestBody Dept dept) {
        log.info("添加部门数据{}", dept.getName());
        return deptService.addDept(dept) > 0 ? Result.success() : Result.error("添加失败");
    }

    @GetMapping("/{id}")
    public Result getDeptById(@PathVariable Integer id) {
        log.info("根据id查询部门数据：{}",  id);
        return Result.success(deptService.getDeptById(id));
    }

    @PutMapping
    public Result update(@RequestBody Dept dept) {
        log.info("更新部门数据，部门id：{}，更新为name：{}", dept.getId(), dept.getName());
        return deptService.updateDept(dept) > 0 ? Result.success() : Result.error("更新失败");
    }
}
