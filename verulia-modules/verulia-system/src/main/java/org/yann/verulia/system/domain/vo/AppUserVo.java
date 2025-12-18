package org.yann.verulia.system.domain.vo;


import java.io.Serializable;

/**
 *
 * @author Yann
 * @date 2025/12/18 22:08
 */
public record AppUserVo(
    Long id,
    String nickname,
    String avatar,
    Integer level
) implements Serializable {
}
