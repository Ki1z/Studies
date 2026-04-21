package com.eiousee.mapper;

import com.eiousee.pojo.StuQueryParam;
import com.eiousee.pojo.Student;
import com.eiousee.pojo.StudentDiscipline;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {
    @Select("SELECT major_name FROM major")
    List<String> getMajor();

    @Select("SELECT education_name FROM education")
    List<String> getEducation();

    List<Student> list(StuQueryParam queryParam);

    Integer deleteStudentByIds(Integer[] ids);

    @Insert("""
            INSERT INTO
            student(id, name, sex, birth, class_id, major_id, education_id, update_time)
            VALUES (
                    #{id},
                    #{name},
                    #{sex},
                    #{birth},
                    (SELECT id FROM class WHERE class_name = #{className}),
                    (SELECT id FROM major WHERE major_name = #{majorName}),
                    (SELECT id FROM education WHERE education_name = #{educationName}),
                    #{updateTime}
            )
            """)
    Integer addStudent(Student student);

    Student getStudentById(Integer id);

    Integer updateStudent(Student student);

    List<StudentDiscipline> getStudentDiscipline(Integer id);

    @Delete("DELETE FROM student_discipline WHERE student_id = #{id}")
    void deleteStudentDiscipline(Integer id);

    void updateStudentDiscipline(List<StudentDiscipline> studentDiscipline);
}
