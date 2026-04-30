package com.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.entity.Attendance;
import com.oa.mapper.AttendanceMapper;
import com.oa.service.AttendanceService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {

    private static final LocalTime CHECK_IN_TIME = LocalTime.of(9, 0);
    private static final LocalTime CHECK_OUT_TIME = LocalTime.of(18, 0);

    @Override
    public Attendance checkIn(Long userId) {
        LocalDate today = LocalDate.now();
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getUserId, userId);
        wrapper.eq(Attendance::getAttendDate, today);
        Attendance attendance = this.getOne(wrapper);
        
        LocalDateTime now = LocalDateTime.now();
        
        if (attendance == null) {
            attendance = new Attendance();
            attendance.setUserId(userId);
            attendance.setAttendDate(today);
            attendance.setCheckInTime(now);
            attendance.setDeleted("0");
            
            if (now.toLocalTime().isAfter(CHECK_IN_TIME)) {
                attendance.setCheckInStatus("1");
            } else {
                attendance.setCheckInStatus("0");
            }
            attendance.setStatus("0");
            this.save(attendance);
        } else {
            if (attendance.getCheckInTime() != null) {
                throw new RuntimeException("今天已经签到过了");
            }
            attendance.setCheckInTime(now);
            if (now.toLocalTime().isAfter(CHECK_IN_TIME)) {
                attendance.setCheckInStatus("1");
            } else {
                attendance.setCheckInStatus("0");
            }
            this.updateById(attendance);
        }
        return attendance;
    }

    @Override
    public Attendance checkOut(Long userId) {
        LocalDate today = LocalDate.now();
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getUserId, userId);
        wrapper.eq(Attendance::getAttendDate, today);
        Attendance attendance = this.getOne(wrapper);
        
        if (attendance == null || attendance.getCheckInTime() == null) {
            throw new RuntimeException("请先签到");
        }
        
        if (attendance.getCheckOutTime() != null) {
            throw new RuntimeException("今天已经签退过了");
        }
        
        LocalDateTime now = LocalDateTime.now();
        attendance.setCheckOutTime(now);
        
        if (now.toLocalTime().isBefore(CHECK_OUT_TIME)) {
            attendance.setCheckOutStatus("1");
        } else {
            attendance.setCheckOutStatus("0");
        }
        
        this.updateById(attendance);
        return attendance;
    }

    @Override
    public Attendance getTodayRecord(Long userId) {
        LocalDate today = LocalDate.now();
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getUserId, userId);
        wrapper.eq(Attendance::getAttendDate, today);
        wrapper.eq(Attendance::getDeleted, "0");
        return this.getOne(wrapper);
    }

    @Override
    public Page<Attendance> getMyRecords(Page<Attendance> page, Long userId, Attendance attendance) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getUserId, userId);
        if (attendance != null) {
            if (attendance.getAttendDate() != null) {
                wrapper.eq(Attendance::getAttendDate, attendance.getAttendDate());
            }
            if (StringUtils.hasText(attendance.getStatus())) {
                wrapper.eq(Attendance::getStatus, attendance.getStatus());
            }
        }
        wrapper.eq(Attendance::getDeleted, "0");
        wrapper.orderByDesc(Attendance::getAttendDate);
        return this.page(page, wrapper);
    }

    @Override
    public Page<Attendance> pageRecords(Page<Attendance> page, Attendance attendance) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        if (attendance != null) {
            if (attendance.getUserId() != null) {
                wrapper.eq(Attendance::getUserId, attendance.getUserId());
            }
            if (attendance.getAttendDate() != null) {
                wrapper.eq(Attendance::getAttendDate, attendance.getAttendDate());
            }
            if (StringUtils.hasText(attendance.getStatus())) {
                wrapper.eq(Attendance::getStatus, attendance.getStatus());
            }
        }
        wrapper.eq(Attendance::getDeleted, "0");
        wrapper.orderByDesc(Attendance::getAttendDate);
        return this.page(page, wrapper);
    }
}