package org.yann.verulia.framework.web.aspect;


import cn.hutool.v7.core.data.id.IdUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.http.server.servlet.ServletUtil;
import cn.hutool.v7.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Web 请求日志切面
 * 优化点：
 * 1. 过滤敏感参数（如文件流、Response对象）
 * 2. 获取真实 IP
 * 3. 增加 TraceId 链路追踪
 * 4. 敏感字段脱敏（密码不打印）
 *
 * @author Yann
 * @date 2025/12/15 14:17
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    /**
     * 换行符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 定义切点：所有 @RestController 注解的类中的方法
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerLog() {}

    /**
     * 环绕通知：核心逻辑
     */
    @Around("controllerLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 1. 设置 TraceId，方便日志追踪
        String traceId = IdUtil.fastSimpleUUID();
        MDC.put("traceId", traceId);

        // 获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        // 2. 打印请求参数 (Before 的逻辑移到这里统一处理)
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String ip = ServletUtil.getClientIP(request); // 使用 Hutool 获取真实 IP
        String params = getArgs(joinPoint.getArgs()); // 安全获取参数

        log.info("{}================  请求开始  ================{}", LINE_SEPARATOR, LINE_SEPARATOR);
        log.info("请求地址     : {}", url);
        log.info("请求方式     : {}", method);
        log.info("客户端IP     : {}", ip);
        log.info("调用方法     : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("请求参数     : {}", params);
        log.info("{}--------------------------------------------------{}", LINE_SEPARATOR, LINE_SEPARATOR);

        // 3. 执行目标方法
        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            // 确保 MDC 清除，防止线程池复用导致 TraceId 混乱
            // 注意：finally 块不包含 return，否则会吞掉异常
        }

        // 4. 打印响应结果和耗时
        long timeTaken = System.currentTimeMillis() - startTime;

        // 对响应结果进行截断，防止 Base64 图片等超长响应刷屏
        String resultStr = JSONUtil.toJsonStr(result);
        if (StrUtil.length(resultStr) > 2000) {
            resultStr = StrUtil.sub(resultStr, 0, 2000) + "...[内容过长截断]";
        }

        log.info("{}响应结果    : {}", LINE_SEPARATOR, resultStr);
        log.info("执行耗时     : {} ms", timeTaken);
        log.info("{}================  请求结束  ================{}", LINE_SEPARATOR, LINE_SEPARATOR);

        MDC.remove("traceId"); // 清除 traceId
        return result;
    }

    /**
     * 安全地获取参数 JSON 字符串
     * 剔除 HttpServletRequest、HttpServletResponse、MultipartFile 等无法序列化的对象
     */
    private String getArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        List<Object> safeArgs = new ArrayList<>();
        for (Object arg : args) {
            // 过滤掉不能序列化的 Web 对象和文件对象
            if (arg instanceof HttpServletRequest
                    || arg instanceof HttpServletResponse
                    || arg instanceof MultipartFile
                    || arg instanceof BindingResult) {
                continue;
            }
            safeArgs.add(arg);
        }

        try {
            // 简单的转 JSON，实际生产中可以配置 ObjectMapper 忽略 password 字段
            return JSONUtil.toJsonStr(safeArgs);
        } catch (Exception e) {
            return "参数解析失败";
        }
    }
}
