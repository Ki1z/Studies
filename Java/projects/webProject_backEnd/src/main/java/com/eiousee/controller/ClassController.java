package com.eiousee.controller;

import com.eiousee.pojo.ClassListQueryParam;
import com.eiousee.pojo.ClassPojo;
import com.eiousee.pojo.Result;
import com.eiousee.pojo.ClassQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.eiousee.service.ClassService;

@Slf4j
@RestController
@RequestMapping("/class")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public Result list(ClassQueryParam queryParam) {
        log.info("查询班级列表：{}", queryParam);
        return Result.success(classService.list(queryParam));
    }

    @DeleteMapping("/{id}")
    public Result deleteClassById(@PathVariable Integer id) {
        log.info("删除班级：{}", id);
        return classService.deleteClassById(id) > 0 ? Result.success() : Result.error("未找到该班级");
    }

    @GetMapping("/status")
    public Result getClassStatus() {
        log.info("查询班级状态列表");
        return Result.success(classService.getClassStatus());
    }

    @PostMapping
    public Result addClass(@RequestBody ClassPojo classPojo) {
        log.info("添加班级：{}", classPojo);
        return classService.addClass(classPojo) > 0 ? Result.success() : Result.error("添加班级失败");
    }

    @GetMapping("/{id}")
    public Result getClassById(@PathVariable Integer id) {
        log.info("通过id查询班级：{}", id);
        return Result.success(classService.getClassById(id));
    }

    @PutMapping
    public Result updateClass(@RequestBody ClassPojo classPojo) {
        log.info("更新班级：{}", classPojo);
        return classService.updateClass(classPojo) > 0 ? Result.success() : Result.error("更新班级失败");
    }

    @GetMapping("/list")
    public Result list(ClassListQueryParam queryParam) {
        log.info("查询班级列表");
        return Result.success(classService.listAll(queryParam));
    }
}
