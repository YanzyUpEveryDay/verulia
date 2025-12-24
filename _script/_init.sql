DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`    varchar(50)  NOT NULL COMMENT '用户名',
    `password`    varchar(100) NOT NULL COMMENT '密码',
    `nickname`    varchar(50)  DEFAULT NULL COMMENT '昵称',
    `avatar`      varchar(500) DEFAULT NULL COMMENT '头像',
    `email`       varchar(100) DEFAULT NULL COMMENT '邮箱',
    `phone`       varchar(20)  DEFAULT NULL COMMENT '手机号',
    `sex`         tinyint      DEFAULT '0' COMMENT '性别 0=未知 1=男 2=女',
    `status`      tinyint      DEFAULT '1' COMMENT '状态 0=禁用 1=正常',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    `deleted`     tinyint      DEFAULT '0' COMMENT '逻辑删除 0=未删除 1=已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `role_id`     bigint       NOT NULL COMMENT '角色ID',
    `role_name`   varchar(30)  NOT NULL COMMENT '角色名称',
    `role_key`    varchar(100) NOT NULL COMMENT '权限字符',
    `role_sort`   int          NOT NULL DEFAULT '0' COMMENT '显示顺序',
    `status`      tinyint      NOT NULL DEFAULT '1' COMMENT '角色状态（1正常 0停用）',
    `deleted`     tinyint               DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    `create_by`   varchar(64)           DEFAULT '' COMMENT '创建者',
    `create_time` datetime              DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64)           DEFAULT '' COMMENT '更新者',
    `update_time` datetime              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`role_id`),
    UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

DROP TABLE IF EXISTS `sys_social_auth`;
CREATE TABLE `sys_social_auth`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `user_id`     bigint                                  DEFAULT NULL COMMENT '用户ID',
    `openid`      varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'openid',
    `unionid`     varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'unionid',
    `source`      varchar(20) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '来源',
    `create_time` datetime                                DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='第三方认证信息';