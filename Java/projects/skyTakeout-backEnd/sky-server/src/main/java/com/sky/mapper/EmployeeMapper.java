package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 插入员工数据
     * @param employee
     */
    @AutoFill(OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     */
    List<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更改员工账号状态
     * @param id
     * @return
     */
    @Update("update employee set status = #{status} where id = #{id}")
    Integer changeStatus(Integer status, Long id);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Select("select id, name, username, phone, sex, id_number, status, create_time, update_time, create_user, update_user" +
            " from employee where id = #{id}")
    Employee getById(Long id);

    /**
     * 编辑员工信息
     * @param employee
     * @return
     */
    @AutoFill(OperationType.UPDATE)
    Integer update(Employee employee);
}
