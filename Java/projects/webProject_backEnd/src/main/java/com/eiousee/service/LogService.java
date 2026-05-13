package com.eiousee.service;

import com.eiousee.pojo.OperationLog;
import com.eiousee.pojo.PageResult;

public interface LogService {
    PageResult<OperationLog> getLogs(Integer pageNum, Integer pageSize);
}
