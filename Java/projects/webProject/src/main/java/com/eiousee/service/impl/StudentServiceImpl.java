package com.eiousee.service.impl;

import com.eiousee.mapper.StudentMapper;
import com.eiousee.pojo.PageResult;
import com.eiousee.pojo.StuQueryParam;
import com.eiousee.pojo.Student;
import com.eiousee.pojo.StudentDiscipline;
import com.eiousee.service.StudentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public List<String> getMajor() {
        return studentMapper.getMajor();
    }

    @Override
    public List<String> getEducation() {
        return studentMapper.getEducation();
    }

    @Override
    public PageResult<Student> getStudentList(StuQueryParam queryParam) {
        Page<Student> page = PageHelper.startPage(queryParam.getPage(), queryParam.getPageSize());
        List<Student> list = studentMapper.list(queryParam);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public Integer deleteStudentByIds(Integer[] ids) {
        return studentMapper.deleteStudentByIds(ids);
    }

    @Override
    public Integer addStudent(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        return studentMapper.addStudent(student);
    }

    @Override
    public Student getStudentById(Integer id) {
        return studentMapper.getStudentById(id);
    }

    @Override
    public Integer updateStudent(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        return studentMapper.updateStudent(student);
    }

    @Override
    public List<StudentDiscipline> getStudentDiscipline(Integer id) {
        return studentMapper.getStudentDiscipline(id);
    }

    @Override
    @Transactional
    public void updateStudentDiscipline(Integer id, List<StudentDiscipline> studentDiscipline) {
        studentMapper.deleteStudentDiscipline(id);
        if (!studentDiscipline.isEmpty()) {
            studentMapper.updateStudentDiscipline(studentDiscipline);
        }
        Student student = studentMapper.getStudentById(id);
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.updateStudent(student);
    }
}
