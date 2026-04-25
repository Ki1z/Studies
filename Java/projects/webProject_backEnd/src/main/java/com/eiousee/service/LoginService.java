package com.eiousee.service;

import com.eiousee.pojo.LoginInfo;
import com.eiousee.pojo.LoginResult;

public interface LoginService {
    LoginResult login(LoginInfo loginInfo);
}
