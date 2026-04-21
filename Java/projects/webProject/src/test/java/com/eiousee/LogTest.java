package com.eiousee;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    private static final Logger log = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void test1() {
        log.debug("开始计算");

        Integer sum = 0;
        Integer[] nums = {1, 2, 3, 4, 5};
        for (Integer num : nums) {
            sum += num;
        }

        log.info("结果为：{}", sum);
        log.debug("结束计算");
    }
}
