package com.sky.service;

public interface ShopService {

    /**
     * 获取店铺的营业状态
     * @return
     */
    void changeStatus(Integer status);

    /**
     * 获取店铺的营业状态
     * @return
     */
    Integer getStatus();
}
