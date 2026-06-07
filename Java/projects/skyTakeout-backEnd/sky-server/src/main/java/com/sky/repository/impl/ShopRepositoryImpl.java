package com.sky.repository.impl;

import com.sky.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class ShopRepositoryImpl implements ShopRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String KEY_SHOP_STATUS = "SHOP:STATUS";

    /**
     * 改变店铺状态
     * @param status
     * @return
     */
    @Override
    public Integer changeStatus(Integer status) {
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set(KEY_SHOP_STATUS, status);
        return Objects.equals(ops.get(KEY_SHOP_STATUS), status) ? 1 : 0;
    }

    /**
     * 获取店铺状态
     * @return
     */
    @Override
    public Integer getStatus() {
        ValueOperations ops = redisTemplate.opsForValue();
        return (Integer) ops.get(KEY_SHOP_STATUS);
    }
}
