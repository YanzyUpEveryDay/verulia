package org.yann.verulia.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yann.verulia.framework.core.domain.PageResult;
import org.yann.verulia.framework.core.domain.R;
import org.yann.verulia.system.domain.dto.MemberDtos;
import org.yann.verulia.system.service.IAppMemberService;

/**
 * 会员管理 Controller
 *
 * @author Yann
 */
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class AppMemberController {

    private final IAppMemberService appMemberService;

    /**
     * 分页查询会员列表
     */
    @GetMapping("/page")
    public R<PageResult<MemberDtos.Result>> page(MemberDtos.Query queryDto) {
        return R.ok(appMemberService.pageQuery(queryDto));
    }

    /**
     * 根据ID获取会员详情
     */
    @GetMapping("/{id}")
    public R<MemberDtos.Result> getInfo(@PathVariable Long id) {
        return R.ok(appMemberService.getMemberById(id));
    }

    /**
     * 新增会员
     */
    @PostMapping
    public R<Void> add(@RequestBody MemberDtos.Create createParams) {
        appMemberService.addMember(createParams);
        return R.ok();
    }

    /**
     * 修改会员
     */
    @PutMapping
    public R<Void> edit(@RequestBody MemberDtos.Update updateParams) {
        appMemberService.updateMember(updateParams);
        return R.ok();
    }

    /**
     * 删除会员
     */
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        appMemberService.deleteMember(id);
        return R.ok();
    }

    /**
     * 修改会员状态
     */
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(@PathVariable Long id, @RequestBody MemberDtos.Update updateParams) {
        appMemberService.updateStatus(id, updateParams.status());
        return R.ok();
    }
}
