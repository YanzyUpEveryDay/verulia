package org.yann.verulia.framework.auth.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录表单
 *
 * @author Yann
 */
@Data
public class LoginBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 授权类型 (必须)
     * 默认值: password
     */
    @NotBlank(message = "授权类型不能为空")
    private String grantType = "password";

    /**
     * 用户名 (密码模式)
     */
    private String username;

    /**
     * 密码 (密码模式)
     */
    private String password;

    /**
     * 验证码 (密码模式/短信模式)
     * 图形验证码 或 短信验证码
     */
    private String code;

    /**
     * 唯一标识 (图形验证码的 UUID)
     */
    private String uuid;

    /**
     * 手机号 (短信模式)
     */
    private String phonenumber;

    // 如果未来有微信登录，可能还需要:
    // private String openid;
    // private String iv;
    // private String encryptedData;
}
