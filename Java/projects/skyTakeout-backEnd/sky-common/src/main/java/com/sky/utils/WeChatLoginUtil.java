package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import com.sky.properties.WeChatProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class WeChatLoginUtil {

    private final WeChatProperties weChatProperties;

    private static final String WECHAT_LOGIN_API = "https://api.weixin.qq.com/sns/jscode2session";

    public String getOpenId(String code) {
        Map<String, String> LoginParam = new HashMap<>();
        // appid
        LoginParam.put("appid", weChatProperties.getAppid());
        // appsecret
        LoginParam.put("secret", weChatProperties.getSecret());
        // 登录code
        LoginParam.put("js_code", code);
        // 授权类型
        LoginParam.put("grant_type", "authorization_code");

        // 发送请求
        String json = HttpClientUtil.doGet(WECHAT_LOGIN_API, LoginParam);
        log.info("微信返回的json数据：{}", json);
        // 解析json数据
        JSONObject jsonObject = JSONObject.parseObject(json);
        return jsonObject.getString("openid");
    }
}
