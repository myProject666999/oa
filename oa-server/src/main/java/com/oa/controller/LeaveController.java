package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oa.common.Result;
import com.oa.entity.Leave;
import com.oa.security.LoginUser;
import com.oa.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/apply")
    public Result<Leave> apply(@RequestBody Leave leave,
                               @AuthenticationPrincipal LoginUser loginUser) {
        Leave result = leaveService.apply(leave, loginUser.getUser().getId());
        return Result.success(result);
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('leave:approve')")
    public Result<Boolean> approve(@RequestParam Long id,
                                    @RequestParam String status,
                                    @RequestParam(required = false) String comment,
                                    @AuthenticationPrincipal LoginUser loginUser) {
        boolean result = leaveService.approve(id, status, loginUser.getUser().getId(), comment);
        return Result.success(result);
    }

    @GetMapping("/myList")
    public Result<Page<Leave>> myList(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Page<Leave> page = new Page<>(current, size);
        Page<Leave> result = leaveService.getMyLeaveList(page, loginUser.getUser().getId());
        return Result.success(result);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('leave:list')")
    public Result<Page<Leave>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Leave leave) {
        Page<Leave> page = new Page<>(current, size);
        LambdaQueryWrapper<Leave> wrapper = new LambdaQueryWrapper<>();
        if (leave != null) {
            if (StringUtils.hasText(leave.getStatus())) {
                wrapper.eq(Leave::getStatus, leave.getStatus());
            }
            if (leave.getUserId() != null) {
                wrapper.eq(Leave::getUserId, leave.getUserId());
            }
        }
        wrapper.eq(Leave::getDeleted, "0");
        wrapper.orderByDesc(Leave::getCreateTime);
        Page<Leave> result = leaveService.page(page, wrapper);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Leave> getById(@PathVariable Long id) {
        Leave leave = leaveService.getById(id);
        return Result.success(leave);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id,
                                  @AuthenticationPrincipal LoginUser loginUser) {
        Leave leave = leaveService.getById(id);
        if (leave == null) {
            return Result.error("请假申请不存在");
        }
        if (!"0".equals(leave.getStatus())) {
            return Result.error("只能撤销待审批的申请");
        }
        if (!leave.getUserId().equals(loginUser.getUser().getId())) {
            return Result.error("只能撤销自己的申请");
        }
        leave.setDeleted("1");
        boolean result = leaveService.updateById(leave);
        return Result.success(result);
    }
}
