package com.oa.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.entity.Leave;

public interface LeaveService extends IService<Leave> {

    Leave apply(Leave leave, Long userId);

    boolean approve(Long id, String status, Long userId, String comment);

    Page<Leave> getMyLeaveList(Page<Leave> page, Long userId);
}
