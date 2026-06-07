package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.exception.BaseException;
import com.sky.exception.FormValueIsNullException;
import com.sky.exception.ShopStatusChangeFailedException;
import com.sky.exception.ShopStatusGetFailedException;
import com.sky.repository.ShopRepository;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sky.constant.MessageConstant;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopRepository shopRepository;

    /**
     * 改变店铺状态
     *
     * @param status
     */
    @Override
    public void changeStatus(Integer status) {
        if (status == null || !Objects.equals(status, StatusConstant.ENABLE) && !Objects.equals(status, StatusConstant.DISABLE))
            throw new FormValueIsNullException(MessageConstant.STATUS_IS_NOT_DEFINED);
        Integer count = shopRepository.changeStatus(status);
        if (count == 0)
            throw new ShopStatusChangeFailedException(MessageConstant.SHOP_STATUS_CHANGE_FAILED);
    }

    /**
     * 获取店铺状态
     *
     * @return
     */
    @Override
    public Integer getStatus() {
        Integer status = shopRepository.getStatus();
        if (status == null || !Objects.equals(status, StatusConstant.ENABLE) && !Objects.equals(status, StatusConstant.DISABLE))
            throw new ShopStatusGetFailedException(MessageConstant.STATUS_IS_NOT_DEFINED);

        return status;
    }
}
