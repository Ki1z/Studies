package com.sky.repository;

public interface ShopRepository {
    /**
     * 修改店铺状态
     * @param status
     * @return
     */
    Integer changeStatus(Integer status);

    /**
     * 获取店铺状态
     * @return
     */
    Integer getStatus();
}
