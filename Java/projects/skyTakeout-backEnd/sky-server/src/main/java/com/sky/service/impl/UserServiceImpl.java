package com.sky.service.impl;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.BaseException;
import com.sky.exception.CannotGetOpenIdException;
import com.sky.exception.FormValueIsNullException;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.utils.WeChatLoginUtil;
import com.sky.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatLoginUtil weChatLoginUtil;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 参数校验
        String code = userLoginDTO.getCode();
        if (code == null)
            throw new FormValueIsNullException(MessageConstant.LOGIN_CODE_IS_NULL);
        if (code.length() != 32 && !code.startsWith("0"))
            throw new FormValueIsNullException(MessageConstant.LOGIN_CODE_FORMATE_ERROR);

        // 获取openid
        String openid = weChatLoginUtil.getOpenId(code);
        if (openid == null)
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);

        // 判断是否为新用户
        User user = userMapper.getByOpenid(openid);
        // 新用户完成自动注册
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            Integer count = userMapper.add(user);
            if (count <= 0)
                throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 创建jwt令牌
        Map<String, Object> userClaim = new HashMap<>();
        userClaim.put(JwtClaimsConstant.USER_ID, user.getId());
        userClaim.put(jwtProperties.getUserTokenName(), openid);
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                userClaim);

        return UserLoginVO.builder()
                .id(user.getId())
                .openid(openid)
                .token(token)
                .build();
    }

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    @Override
    public User getById(Long userId) {
        if (userId == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_RECOGNIZE_USER);
        return userMapper.getById(userId);
    }
}
