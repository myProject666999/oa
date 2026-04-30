package com.oa.config;

import com.oa.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Boolean> handleException(Exception e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<Boolean> handleRuntimeException(RuntimeException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Result<Boolean> handleBadCredentialsException(BadCredentialsException e) {
        return Result.error("用户名或密码错误");
    }

    @ExceptionHandler(LockedException.class)
    public Result<Boolean> handleLockedException(LockedException e) {
        return Result.error("用户已被禁用");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Boolean> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error("没有权限访问该资源");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Boolean> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Result.error("参数校验失败: " + e.getMessage());
    }
}
