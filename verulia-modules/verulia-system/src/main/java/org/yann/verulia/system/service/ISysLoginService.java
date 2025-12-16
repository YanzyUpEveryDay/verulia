package org.yann.verulia.system.service;

import org.yann.verulia.system.domain.dto.LoginBody;
import org.yann.verulia.system.domain.vo.LoginVo;

/**
 * 登录 Service 接口
 *
 * @author Yann
 */
public interface ISysLoginService {

    /**
     * 登录
     *
     * @param loginBody 登录参数
     * @return 登录结果
     */
    LoginVo login(LoginBody loginBody);

    /**
     * 获取当前登录用户信息
     *
     * @param userId 用户ID
     * @return 登录结果 (包含用户信息和角色)
     */
    LoginVo getRemoteInfo(Long userId);
}
