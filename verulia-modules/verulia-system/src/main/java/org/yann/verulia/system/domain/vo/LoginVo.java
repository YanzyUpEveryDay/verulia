package org.yann.verulia.system.domain.vo;

import org.yann.verulia.system.domain.dto.UserDtos;

import java.io.Serializable;
import java.util.List;

/**
 * 登录结果 VO
 *
 * @author Yann
 */
public record LoginVo(
    String token,
    UserDtos.Result userInfo,
    List<String> roles
) implements Serializable {}
