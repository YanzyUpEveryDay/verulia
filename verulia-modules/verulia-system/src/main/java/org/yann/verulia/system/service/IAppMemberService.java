package org.yann.verulia.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.system.domain.dto.MemberDtos;
import org.yann.verulia.system.domain.entity.AppMember;

/**
 * 会员 Service 接口
 *
 * @author Yann
 */
public interface IAppMemberService extends IService<AppMember> {

    /**
     * 分页查询会员列表
     *
     * @param queryDto 查询参数
     * @return 会员列表分页结果
     */
    PageResult<MemberDtos.Result> pageQuery(MemberDtos.Query queryDto);

    /**
     * 根据ID获取会员详情
     *
     * @param id 会员ID
     * @return 会员详情 VO
     */
    MemberDtos.Result getMemberById(Long id);

    /**
     * 新增会员
     *
     * @param createParams 新增参数
     */
    void addMember(MemberDtos.Create createParams);

    /**
     * 修改会员
     *
     * @param updateParams 修改参数
     */
    void updateMember(MemberDtos.Update updateParams);

    /**
     * 删除会员
     *
     * @param id 会员ID
     */
    void deleteMember(Long id);

    /**
     * 修改会员状态
     *
     * @param id     会员ID
     * @param status 状态
     */
    void updateStatus(Long id, String status);

    /**
     * 根据openid判断会员是否存在
     * @param openId  openid
     * @return  是否存在
     */
    boolean hasMemberByOpenId(String openId);

    /**
     * 微信注册会员（或者绑定openid）
     * <p>
     * 如果openid已存在，则返回现有会员信息<br>
     * 如果openid不存在，则创建新会员
     *
     * @param wechatRegister 微信注册参数
     * @return 会员信息
     */
    MemberDtos.Result registerByWechat(MemberDtos.WechatRegister wechatRegister);
}
