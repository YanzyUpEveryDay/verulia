package org.yann.verulia.system.service.impl;

import cn.hutool.v7.core.bean.BeanUtil;
import cn.hutool.v7.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.framework.core.exception.BusinessException;
import org.yann.verulia.system.domain.dto.MemberDtos;
import org.yann.verulia.system.domain.entity.AppMember;
import org.yann.verulia.system.mapper.AppMemberMapper;
import org.yann.verulia.system.service.IAppMemberService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会员 Service 实现
 *
 * @author Yann
 */
@Service
@RequiredArgsConstructor
public class AppMemberServiceImpl extends ServiceImpl<AppMemberMapper, AppMember> implements IAppMemberService {

    @Override
    public PageResult<MemberDtos.Result> pageQuery(MemberDtos.Query queryDto) {
        LambdaQueryWrapper<AppMember> wrapper = new LambdaQueryWrapper<>();
        if (queryDto.getNickname() != null && !queryDto.getNickname().isBlank()) {
            wrapper.like(AppMember::getNickname, queryDto.getNickname());
        }
        if (queryDto.getMobile() != null && !queryDto.getMobile().isBlank()) {
            wrapper.eq(AppMember::getMobile, queryDto.getMobile());
        }
        if (queryDto.getLevel() != null) {
            wrapper.eq(AppMember::getLevel, queryDto.getLevel());
        }
        if (queryDto.getStatus() != null && !queryDto.getStatus().isBlank()) {
            wrapper.eq(AppMember::getStatus, queryDto.getStatus());
        }

        wrapper.orderByDesc(AppMember::getCreateTime);

        Page<AppMember> page = new Page<>(queryDto.getPageNum(), queryDto.getPageSize());
        Page<AppMember> memberPage = this.page(page, wrapper);

        List<MemberDtos.Result> voList = memberPage.getRecords().stream()
            .map(MemberDtos.Result::fromAppMember)
            .collect(Collectors.toList());

        return PageResult.of(voList, memberPage.getTotal());
    }

    @Override
    public MemberDtos.Result getMemberById(Long id) {
        AppMember member = this.getById(id);
        if (member == null) {
            return null;
        }
        return MemberDtos.Result.fromAppMember(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMember(MemberDtos.Create createParams) {
        // 检查手机号是否已存在
        if (createParams.mobile() != null && !createParams.mobile().isBlank()) {
            if (exists(new LambdaQueryWrapper<AppMember>().eq(AppMember::getMobile, createParams.mobile()))) {
                throw new BusinessException("手机号已存在");
            }
        }

        AppMember member = new AppMember();
        BeanUtil.copyProperties(createParams, member);

        // 如果有密码，进行加密
        if (createParams.password() != null && !createParams.password().isBlank()) {
            member.setPassword(SecureUtil.sha256(createParams.password()));
        }

        // 设置默认值
        if (member.getBalance() == null) {
            member.setBalance(BigDecimal.ZERO);
        }
        if (member.getScore() == null) {
            member.setScore(0);
        }
        if (member.getStatus() == null || member.getStatus().isBlank()) {
            member.setStatus("0"); // 默认正常
        }

        if (member.getLevel() == null) {
            member.setLevel(1);
        }

        this.save(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMember(MemberDtos.Update updateParams) {
        AppMember member = this.getById(updateParams.id());
        if (member == null) {
            throw new BusinessException("会员不存在");
        }

        // 检查手机号是否被其他会员使用
        if (updateParams.mobile() != null && !updateParams.mobile().isBlank()) {
            if (exists(new LambdaQueryWrapper<AppMember>()
                .eq(AppMember::getMobile, updateParams.mobile())
                .ne(AppMember::getId, updateParams.id()))) {
                throw new BusinessException("手机号已被其他会员使用");
            }
        }

        if (updateParams.nickname() != null) member.setNickname(updateParams.nickname());
        if (updateParams.mobile() != null) member.setMobile(updateParams.mobile());
        if (updateParams.avatar() != null) member.setAvatar(updateParams.avatar());
        if (updateParams.level() != null) member.setLevel(updateParams.level());
        if (updateParams.status() != null) member.setStatus(updateParams.status());

        this.updateById(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMember(Long id) {
        if (!this.removeById(id)) {
            throw new BusinessException("删除失败，会员不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, String status) {
        AppMember member = this.getById(id);
        if (member == null) {
            throw new BusinessException("会员不存在");
        }
        member.setStatus(status);
        this.updateById(member);
    }

    @Override
    public boolean hasMemberByOpenId(String openId) {
        return this.exists(new LambdaQueryWrapper<AppMember>().eq(AppMember::getOpenid, openId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberDtos.Result registerByWechat(MemberDtos.WechatRegister wechatRegister) {
        // 检查openid是否已存在
        AppMember existingMember = this.getOne(
            new LambdaQueryWrapper<AppMember>()
                .eq(AppMember::getOpenid, wechatRegister.openid())
        );

        // 如果已存在，直接返回
        if (existingMember != null) {
            return MemberDtos.Result.fromAppMember(existingMember);
        }

        // 创建新会员
        AppMember newMember = new AppMember();
        newMember.setOpenid(wechatRegister.openid());
        newMember.setUnionid(wechatRegister.unionid());
        newMember.setNickname(wechatRegister.nickname());
        newMember.setAvatar(wechatRegister.avatar());
        newMember.setRegisterSource(wechatRegister.registerSource());
        
        // 设置默认值
        newMember.setBalance(BigDecimal.ZERO);
        newMember.setScore(0);
        newMember.setLevel(1);
        newMember.setStatus("0");

        this.save(newMember);
        return MemberDtos.Result.fromAppMember(newMember);
    }
}
