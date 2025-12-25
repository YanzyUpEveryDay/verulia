package org.yann.verulia.framework.security;


import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;

/**
 * 管理项目中所有的 StpLogic 账号体系
 *
 * @author Yann
 * @date 2025/12/25 14:30
 */
public class StpKit {

    /**
     * 默认原生会话对象
     */
    public static final StpLogic DEFAULT = StpUtil.stpLogic;

    /**
     * 会员 会话对象
     */
    public static final StpLogic MEMBER = new StpLogic("member") {
        @Override
        public String splicingKeyTokenName() {
            return super.splicingKeyTokenName() + "-member";
        }
    };
}
