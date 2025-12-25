package org.yann.verulia.framework.mybatis.handler;


import cn.hutool.v7.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.yann.verulia.framework.core.service.SecurityContext;

import java.time.LocalDateTime;

/**
 *
 * @author Yann
 * @date 2025/12/16 11:29
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        SecurityContext securityContext = SpringUtil.getBean(SecurityContext.class);
        // 创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        try {
            // 创建人
            this.strictInsertFill(metaObject, "createBy", Long.class, securityContext.getUserId());
        } catch (Exception e) {
            log.error("mybatis-plus数据insert填充出错：{}", e.getMessage());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        SecurityContext securityContext = SpringUtil.getBean(SecurityContext.class);
        // 修改时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        try {
            // 修改人
            this.strictUpdateFill(metaObject, "updateBy", Long.class, securityContext.getUserId());
        } catch (Exception e) {
            log.error("mybatis-plus数据update填充出错：{}", e.getMessage());
        }
    }
}
