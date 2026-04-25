package com.eiousee.service.impl;

import com.eiousee.mapper.LoginMapper;
import com.eiousee.pojo.LoginInfo;
import com.eiousee.pojo.LoginPojo;
import com.eiousee.pojo.LoginResult;
import com.eiousee.service.LoginService;
import com.eiousee.utils.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginMapper loginMapper;

    public LoginServiceImpl(LoginMapper loginMapper) {
        this.loginMapper = loginMapper;
    }

    @Override
    public LoginResult login(LoginInfo loginInfo) {
        LoginPojo loginPojo = loginMapper.login(loginInfo.getUsername());
        if (loginPojo != null && loginPojo.getPassword().equals(loginInfo.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", loginPojo.getUsername());
            claims.put("id", loginPojo.getId());
            return new LoginResult(loginPojo.getId(), loginPojo.getUsername(), loginPojo.getName(), JwtUtils.generateJwt(claims));
        }
        return null;
    }
}
