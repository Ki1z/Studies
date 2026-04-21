package com.eiousee.mapper;

import com.eiousee.pojo.EmpQueryParam;
import com.eiousee.pojo.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmpMapper {
    /**
     * 查询员工列表
     * @param params 查询参数
     * @return 员工列表
     */
    List<Employee> getEmpList(EmpQueryParam params);

    /**
     * 添加员工
     * @param employee 员工信息
     * @param deptId   部门id
     * @param jobId    职位id
     */
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "employee.id")
    @Insert("""
            INSERT INTO emp_info (name, birth, sex, avatar_path, dept_id, job_id, board_date, update_time)
                VALUES (#{employee.name}, #{employee.birth}, #{employee.sex}, #{employee.avatarPath}, #{deptId}, #{jobId}, #{employee.boardDate}, #{employee.updateTime});
            """)
    void addEmp(@Param("employee") Employee employee, Integer deptId, Integer jobId);

    /**
     * 根据员工id列表删除员工
     * @param ids 员工id列表
     * @return 1 表示删除成功，0 表示删除失败
     */
    Integer deleteEmpByIds(@Param("ids") Integer[] ids);

    /**
     * 根据员工id查询员工信息
     * @param id 员工id
     * @return 员工信息
     */
    Employee getEmpById(Integer id);

    /**
     * 根据员工id查询员工信息及员工experience表信息
     * @param id 员工id
     * @return 员工信息及员工experience表信息
     */
    Employee getEmpAndExpById(Integer id);

    /**
     * 更新员工信息
     * @param employee 员工信息
     */
    @Update("""
            UPDATE
                emp_info
            SET
                avatar_path = #{avatarPath},
                board_date = #{boardDate},
                update_time = #{updateTime},
                dept_id = (SELECT id FROM dept WHERE name = #{deptName}),
                job_id = (SELECT id FROM job WHERE title = #{jobName})
            WHERE
                id = #{id};
            """)
    void updateEmp(Employee employee);

    /**
     * 查询员工名称列表
     * @return 员工名称列表
     */
    @Select("SELECT name FROM emp_info")
    List<String> getEmpNames();

    /**
     * 根据部门id查询员工数量
     * @param deptId 部门id
     * @return 员工数量
     */
    @Select("SELECT COUNT(*) FROM emp_info WHERE dept_id = #{deptId}")
    Integer getEmpCountByDeptId(Integer deptId);
}
