package com.oa.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.entity.Attendance;

public interface AttendanceService extends IService<Attendance> {

    Attendance checkIn(Long userId);

    Attendance checkOut(Long userId);

    Attendance getTodayRecord(Long userId);

    Page<Attendance> getMyRecords(Page<Attendance> page, Long userId, Attendance attendance);

    Page<Attendance> pageRecords(Page<Attendance> page, Attendance attendance);
}