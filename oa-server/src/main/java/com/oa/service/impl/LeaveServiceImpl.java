package com.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.entity.Leave;
import com.oa.mapper.LeaveMapper;
import com.oa.service.LeaveService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements LeaveService {

    @Override
    public Leave apply(Leave leave, Long userId) {
        leave.setUserId(userId);
        leave.setStatus("0");
        leave.setCreateTime(LocalDateTime.now());
        leave.setDeleted("0");
        this.save(leave);
        return leave;
    }

    @Override
    public boolean approve(Long id, String status, Long userId, String comment) {
        Leave leave = this.getById(id);
        if (leave == null) {
            return false;
        }
        leave.setStatus(status);
        leave.setApproveBy(userId);
        leave.setApproveComment(comment);
        leave.setApproveTime(LocalDateTime.now());
        return this.updateById(leave);
    }

    @Override
    public Page<Leave> getMyLeaveList(Page<Leave> page, Long userId) {
        LambdaQueryWrapper<Leave> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Leave::getUserId, userId);
        wrapper.eq(Leave::getDeleted, "0");
        wrapper.orderByDesc(Leave::getCreateTime);
        return this.page(page, wrapper);
    }
}
