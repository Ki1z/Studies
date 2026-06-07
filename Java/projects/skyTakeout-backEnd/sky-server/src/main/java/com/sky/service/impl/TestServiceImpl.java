package com.sky.service.impl;

import com.sky.entity.Test;
import com.sky.mapper.TestMapper;
import com.sky.service.TestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestMapper testMapper;

    @Override
    public void test() {
        Test test = Test.builder().name("test").build();
        testMapper.insertTest(test);
        System.out.println(test.getId());
    }
}
