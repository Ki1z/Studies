package com.eiousee.controller;

import com.eiousee.pojo.Result;
import com.eiousee.pojo.StuQueryParam;
import com.eiousee.pojo.Student;
import com.eiousee.pojo.StudentDiscipline;
import com.eiousee.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/major")
    public Result getMajor() {
        log.info("获取专业列表");
        return Result.success(studentService.getMajor());
    }

    @GetMapping("/education")
    public Result getEducation() {
        log.info("获取学历列表");
        return Result.success(studentService.getEducation());
    }

    @GetMapping
    public Result getStudentList(StuQueryParam queryParam) {
        log.info("获取学生列表: {}", queryParam);
        return Result.success(studentService.getStudentList(queryParam));
    }

    @DeleteMapping
    public Result deleteStudentByIds(Integer[] ids) {
        log.info("根据id删除学生：{}", Arrays.toString(ids));
        return studentService.deleteStudentByIds(ids) > 0 ? Result.success() : Result.error("删除失败");
    }

    @PostMapping
    public Result addStudent(@RequestBody Student student) {
        log.info("添加学生：{}", student);
        return studentService.addStudent(student) > 0 ? Result.success() : Result.error("添加失败");
    }

    @GetMapping("/{id}")
    public Result getStudentById(@PathVariable Integer id) {
        log.info("获取学生信息：{}", id);
        return Result.success(studentService.getStudentById(id));
    }

    @PutMapping
    public Result updateStudent(@RequestBody Student student) {
        log.info("更新学生信息：{}", student);
        return studentService.updateStudent(student) > 0 ? Result.success() : Result.error("更新失败");
    }

    @GetMapping("/{id}/discipline")
    public Result getStudentDiscipline(@PathVariable Integer id) {
        log.info("获取学生{})的违纪信息", id);
        return Result.success(studentService.getStudentDiscipline(id));
    }

    @PutMapping("/{id}/discipline")
    public Result updateStudentDiscipline(@PathVariable Integer id, @RequestBody List<StudentDiscipline> studentDiscipline) {
        log.info("更新学生{})的违纪信息：{}", id, studentDiscipline);
        studentService.updateStudentDiscipline(id, studentDiscipline);
        return Result.success();
    }
}
