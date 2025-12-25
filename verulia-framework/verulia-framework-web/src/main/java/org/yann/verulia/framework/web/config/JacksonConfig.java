package org.yann.verulia.framework.web.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

/**
 * Jackson 配置类
 * 将 Long 类型序列化为 String，避免前端 JavaScript 精度丢失问题
 *
 * @author Yann
 */
@AutoConfiguration
public class JacksonConfig {

    @Bean
    @Primary
    public JsonMapper jacksonJsonMapper() {
        // 创建一个模块，专门负责 Long -> String 的转换
        SimpleModule longToStringModule = new SimpleModule();
        // 针对包装类 Long
        longToStringModule.addSerializer(Long.class, ToStringSerializer.instance);
        // 针对基本类型 long
        longToStringModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        // 强制使用 Builder 模式创建
        return JsonMapper.builder()
                .addModule(longToStringModule)
                // 配置特性：不再是 set/enable，而是 configure 或 enable/disable
                // 处理空 Bean 不报错
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                // 开启格式化输出
                .enable(SerializationFeature.INDENT_OUTPUT)
                // 针对 JSON 解析的细粒度控制 (新 API)
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
                // 构建不可变的 Mapper 实例
                .build();
    }
}
