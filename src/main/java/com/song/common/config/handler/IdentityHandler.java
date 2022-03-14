package com.song.common.config.handler;


import cn.hutool.core.util.StrUtil;
import com.song.common.thread.UserThreadLocal;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Song
 * @date 2022/3/11 11:27
 * @Version 1.0
 */
@Configuration
public class IdentityHandler implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String open_id = request.getHeader("open_id");
        if (StrUtil.isEmpty(open_id)) {
            throw new AuthException("身份验证失败");
        }
        UserThreadLocal.setUser(open_id);
        return true;
    }
}
