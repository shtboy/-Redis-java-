package com.song.common.config.handler;

import com.song.common.constant.ErrorCode;
import com.song.common.constant.MethodResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.message.AuthException;

/**
 * @author Song
 * @date 2022/3/11 11:30
 * @Version 1.0
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public MethodResult customException(Exception e) {
        if (e instanceof DuplicateKeyException) {
            return MethodResult.failure(ErrorCode.data_existed, "不可重复提交数据");
        }
        if (e instanceof AuthException) {
            return MethodResult.failure(ErrorCode.unauthorized, e.getMessage());
        }
        log.error("Exception:", e);
        return MethodResult.failure(ErrorCode.http_method_error, e.getMessage());
    }
}
