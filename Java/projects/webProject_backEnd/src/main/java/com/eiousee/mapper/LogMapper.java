package com.eiousee.mapper;

import com.eiousee.pojo.OperationLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {
    void addLog(OperationLog operationLog);

    List<OperationLog> getLogs();
}
