package org.yann.verulia.framework.core.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 通用分页响应对象
 * @param <T> 数据类型
 */
public record PageResult<T>(
        /*
          数据列表
         */
        List<T> rows,

        /*
          总记录数
         */
        long total
) implements Serializable {

    /**
     * 空分页
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(Collections.emptyList(), 0L);
    }

    /**
     * 手动构建（适用于手动分页或流式处理）
     */
    public static <T> PageResult<T> of(List<T> rows, long total) {
        return new PageResult<>(rows, total);
    }
}