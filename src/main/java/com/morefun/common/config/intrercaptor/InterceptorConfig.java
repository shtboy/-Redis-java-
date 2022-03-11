package com.morefun.common.config.intrercaptor;

import com.morefun.common.config.handler.IdentityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author Song
 * @date 2022/3/11 11:40
 * @Version 1.0
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Autowired
    private IdentityHandler identityHandler;


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(identityHandler);
        super.addInterceptors(registry);
    }
}
