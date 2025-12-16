package org.yann.verulia.framework.security.config;


import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.yann.verulia.framework.core.service.SecurityContext;
import org.yann.verulia.framework.security.service.SaTokenSecurityContext;

/**
 * 权限验证
 *
 * @author Yann
 * @date 2025/12/15 16:05
 */
@Slf4j
@AutoConfiguration
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
    }

    @Bean
    public SecurityContext securityContext() {
        return new SaTokenSecurityContext();
    }
}
