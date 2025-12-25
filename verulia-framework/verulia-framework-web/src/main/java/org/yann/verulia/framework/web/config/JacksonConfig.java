package org.yann.verulia.framework.web.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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
    public JsonMapper jsonMapper() {
        // 创建一个模块，专门负责 Long -> String 的转换
        SimpleModule longToStringModule = new SimpleModule();

        // 1. 针对包装类 Long
        longToStringModule.addSerializer(Long.class, ToStringSerializer.instance);
        // 2. 针对基本类型 long
        longToStringModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        // 构建 JsonMapper
        return JsonMapper.builder()
                .addModule(longToStringModule)
                // 施主如果有其他配置（如时间格式）也可以连在后面写
                // .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
    }
}
