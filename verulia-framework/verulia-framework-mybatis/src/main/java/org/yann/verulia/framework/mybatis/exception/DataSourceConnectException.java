package org.yann.verulia.framework.mybatis.exception;


import org.yann.verulia.framework.core.exception.BusinessException;

import java.io.Serial;

/**
 *
 * @author Yann
 * @date 2025/12/16 09:19
 */
public class DataSourceConnectException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DataSourceConnectException() {
        super("数据库连接失败，服务启动终止");
    }
}
