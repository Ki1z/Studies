package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class FieldAutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFill() {}

    @Before("autoFill()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行数据填充");
        // 获取更新或新增的实例
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object object = args[0];
        // 获取当前时间与操作用户
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        // 获取操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);

        if (annotation == null) {
            return;
        }
        // 根据操作类型执行不同的操作逻辑
        if (annotation.value() == OperationType.INSERT) {
            setFieldValue(object, AutoFillConstant.SET_CREATE_TIME, now);
            setFieldValue(object, AutoFillConstant.SET_UPDATE_TIME, now);
            setFieldValue(object, AutoFillConstant.SET_CREATE_USER, currentId);
            setFieldValue(object, AutoFillConstant.SET_UPDATE_USER, currentId);
        } else if (annotation.value() == OperationType.UPDATE) {
            setFieldValue(object, AutoFillConstant.SET_UPDATE_TIME, now);
            setFieldValue(object, AutoFillConstant.SET_UPDATE_USER, currentId);
        }
    }

    private void setFieldValue(Object object, String fieldName, Object param) {
        try {
            // 获取object的Class对象
            Class<?> clazz = object.getClass();
            // 获取object的fieldName字段的setter方法
            Method setter = clazz.getDeclaredMethod(fieldName, param.getClass());
            // 调用setter方法，为字段赋值
            setter.invoke(object, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
