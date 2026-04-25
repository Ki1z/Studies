package com.eiousee.mapper;

import com.eiousee.pojo.LoginPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    LoginPojo login(String username);
}
