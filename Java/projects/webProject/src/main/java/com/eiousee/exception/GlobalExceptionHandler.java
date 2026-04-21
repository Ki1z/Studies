package com.eiousee.exception;

import com.eiousee.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("服务器异常：" + e.getMessage());
        return Result.error("服务器异常");
    }

    @ExceptionHandler
    public Result handleUncategorizedSQLException(UncategorizedSQLException e) {
        log.error("SQL异常：" + e.getMessage());
        if (e.getMessage().contains("'chk_date_order' is violated")) {
            return Result.error("入职时间不能大于离职时间");
        }
        return Result.error("SQL异常");
    }

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("DuplicateKeyException异常：" + e.getMessage());
        // 获取异常信息
        String message = e.getMessage();
        // 获取索引
        int target = message.indexOf("Duplicate entry");
        // 从索引截取字符串
        String errMsg = message.substring(target);
        return Result.error(errMsg.split(" ")[2] + "已存在");
    }

}
