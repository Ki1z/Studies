package com.eiousee.service.impl;

import com.eiousee.mapper.LogMapper;
import com.eiousee.pojo.OperationLog;
import com.eiousee.pojo.PageResult;
import com.eiousee.service.LogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    private final LogMapper logMapper;

    public LogServiceImpl(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public PageResult<OperationLog> getLogs(Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;

        Page<OperationLog> page = PageHelper.startPage(pageNum, pageSize);
        logMapper.getLogs();
        return new PageResult<>(page.getTotal(), page.getResult());
    }
}
