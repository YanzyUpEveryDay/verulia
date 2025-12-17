package org.yann.verulia.framework.auth.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录表单
 *
 * @author Yann
 */
@Data
public class LoginBody implements Serializable {

    @NotBlank(message = "授权类型不能为空")
    private String grantType = "password";

    private String username;

    private String password;

    private String code;

    private String uuid;

    private String phonenumber;
}
