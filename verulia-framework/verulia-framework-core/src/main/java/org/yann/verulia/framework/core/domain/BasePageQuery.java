package org.yann.verulia.framework.core.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询基类
 * 施主注意：因为 Record 不能继承，所以 QueryDTO 建议使用 Class + Lombok
 */
@Data
public class BasePageQuery implements Serializable {

    /**
     * 当前页码 (默认1)
     */
    private Integer pageNum = 1;

    /**
     * 每页条数 (默认10)
     */
    private Integer pageSize = 10;

    /**
     * 排序字段 (可选)
     */
    private String orderByColumn;

    /**
     * 排序方式 (asc/desc)
     */
    private String isAsc;
}