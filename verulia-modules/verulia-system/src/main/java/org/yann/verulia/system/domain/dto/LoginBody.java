package org.yann.verulia.system.domain.dto;

import java.io.Serializable;

/**
 * 登录表单
 *
 * @author Yann
 */
public record LoginBody(
    String username,
    String password
) implements Serializable {}
