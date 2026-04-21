package com.eiousee.service;

import com.eiousee.pojo.PageResult;
import com.eiousee.pojo.StuQueryParam;
import com.eiousee.pojo.Student;
import com.eiousee.pojo.StudentDiscipline;
import com.github.pagehelper.Page;

import java.util.List;

public interface StudentService {
    List<String> getMajor();

    List<String> getEducation();

    PageResult<Student> getStudentList(StuQueryParam queryParam);

    Integer deleteStudentByIds(Integer[] ids);

    Integer addStudent(Student student);

    Student getStudentById(Integer id);

    Integer updateStudent(Student student);

    List<StudentDiscipline> getStudentDiscipline(Integer id);

    void updateStudentDiscipline(Integer id, List<StudentDiscipline> studentDiscipline);
}
