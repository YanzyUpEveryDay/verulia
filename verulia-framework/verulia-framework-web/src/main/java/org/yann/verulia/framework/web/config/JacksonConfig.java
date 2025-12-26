package org.yann.verulia.framework.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 配置类
 * 将 Long 类型序列化为 String，避免前端 JavaScript 精度丢失问题
 *
 * @author Yann
 */
@Slf4j
@AutoConfiguration
public class JacksonConfig {

    @Value("${spring.jackson.date-format: yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    /**
     * jackson配置
     */
    @Bean
    public JsonMapperBuilderCustomizer moduleCustomizer() {
        return builder -> {
            builder.addModule(numberToStringModule());
            builder.addModule(dateTimeToStringModule());
            log.info("===>> 初始化 Jackson 配置.");
        };
    }

    /**
     * 数字 转 String
     */
    public SimpleModule numberToStringModule() {
        SimpleModule module = new SimpleModule();
        // long
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        // integer
        module.addSerializer(BigInteger.class, ToStringSerializer.instance);
        // decimal
        module.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        return module;
    }

    /**
     * localDateTime 转 String
     */
    public SimpleModule dateTimeToStringModule() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        return module;
    }
}
