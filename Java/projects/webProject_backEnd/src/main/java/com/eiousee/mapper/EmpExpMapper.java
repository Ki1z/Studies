package com.eiousee.mapper;

import com.eiousee.pojo.EmpExp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExpMapper {
    /**
     * 批量插入员工eperience表
     * @param empExpList
     * @return 插入的行数
     */
    Integer addEmpExp(List<EmpExp> empExpList);

    /**
     * 根据员工id批量删除员工eperience表
     * @param ids
     * @return 删除的行数
     */
    Integer deleteEmpExpByIds(Integer[] ids);

    /**
     * 根据员工id查询员工experience表
     * @param id
     * @return 员工experience表
     */
    List<EmpExp> getEmpExpListByEmpId(Integer id);
}
