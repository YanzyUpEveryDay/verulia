package org.yann.verulia.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yann.verulia.system.domain.entity.SysUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 登录结果 VO
 *
 * @author Yann
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户信息
     */
    private UserInfoVo user;
    /**
     * 角色信息
     */
    private Set<String> roles;
    /**
     * 权限信息
     */
    private Set<String> permissions;

    @Data
    @Builder
    public static class UserInfoVo {
        private Long userId;
        /**
         * 用户名
         */
        private String username;
        /**
         * 昵称
         */
        private String nickname;
        /**
         * 头像
         */
        private String avatar;
        /**
         * 手机号
         */
        private String phone;
        /**
         * 性别
         */
        private String sex;

        public static UserInfoVo fromSysUser(SysUser user) {
            return UserInfoVo.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .sex(user.getSex() != null ? user.getSex().toString() : null)
                .build();
        }
    }
}
