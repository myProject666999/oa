package com.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.entity.Attendance;
import com.oa.entity.AttendanceStatistic;
import com.oa.entity.User;
import com.oa.mapper.AttendanceStatisticMapper;
import com.oa.service.AttendanceService;
import com.oa.service.AttendanceStatisticService;
import com.oa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class AttendanceStatisticServiceImpl extends ServiceImpl<AttendanceStatisticMapper, AttendanceStatistic> implements AttendanceStatisticService {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    @Override
    public Page<AttendanceStatistic> pageStatistics(Page<AttendanceStatistic> page, AttendanceStatistic statistic) {
        LambdaQueryWrapper<AttendanceStatistic> wrapper = new LambdaQueryWrapper<>();
        if (statistic != null) {
            if (statistic.getUserId() != null) {
                wrapper.eq(AttendanceStatistic::getUserId, statistic.getUserId());
            }
            if (statistic.getYear() != null) {
                wrapper.eq(AttendanceStatistic::getYear, statistic.getYear());
            }
            if (statistic.getMonth() != null) {
                wrapper.eq(AttendanceStatistic::getMonth, statistic.getMonth());
            }
        }
        wrapper.eq(AttendanceStatistic::getDeleted, "0");
        wrapper.orderByDesc(AttendanceStatistic::getYear);
        wrapper.orderByDesc(AttendanceStatistic::getMonth);
        return this.page(page, wrapper);
    }

    @Override
    public List<AttendanceStatistic> getMyStatistics(Long userId, Integer year, Integer month) {
        LambdaQueryWrapper<AttendanceStatistic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttendanceStatistic::getUserId, userId);
        if (year != null) {
            wrapper.eq(AttendanceStatistic::getYear, year);
        }
        if (month != null) {
            wrapper.eq(AttendanceStatistic::getMonth, month);
        }
        wrapper.eq(AttendanceStatistic::getDeleted, "0");
        wrapper.orderByDesc(AttendanceStatistic::getYear);
        wrapper.orderByDesc(AttendanceStatistic::getMonth);
        return this.list(wrapper);
    }

    @Override
    public void generateMonthlyStatistics(Integer year, Integer month) {
        List<User> users = userService.list(new LambdaQueryWrapper<User>()
                .eq(User::getStatus, "0")
                .eq(User::getDeleted, "0"));

        for (User user : users) {
            calculateUserStatistics(user.getId(), year, month);
        }
    }

    private void calculateUserStatistics(Long userId, Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getUserId, userId);
        wrapper.between(Attendance::getAttendDate, startDate, endDate);
        wrapper.eq(Attendance::getDeleted, "0");
        List<Attendance> attendances = attendanceService.list(wrapper);

        int workDays = yearMonth.lengthOfMonth();
        int attendDays = 0;
        int lateDays = 0;
        int earlyDays = 0;
        int absenceDays = 0;

        for (Attendance attendance : attendances) {
            if (attendance.getCheckInTime() != null) {
                attendDays++;
                if ("1".equals(attendance.getCheckInStatus())) {
                    lateDays++;
                }
                if ("1".equals(attendance.getCheckOutStatus())) {
                    earlyDays++;
                }
            }
        }

        absenceDays = workDays - attendDays;

        LambdaQueryWrapper<AttendanceStatistic> statWrapper = new LambdaQueryWrapper<>();
        statWrapper.eq(AttendanceStatistic::getUserId, userId);
        statWrapper.eq(AttendanceStatistic::getYear, year);
        statWrapper.eq(AttendanceStatistic::getMonth, month);
        AttendanceStatistic statistic = this.getOne(statWrapper);

        if (statistic == null) {
            statistic = new AttendanceStatistic();
            statistic.setUserId(userId);
            statistic.setYear(year);
            statistic.setMonth(month);
            statistic.setWorkDays(workDays);
            statistic.setAttendDays(attendDays);
            statistic.setLateDays(lateDays);
            statistic.setEarlyDays(earlyDays);
            statistic.setAbsenceDays(absenceDays);
            statistic.setLeaveDays(0);
            statistic.setStatus("0");
            statistic.setDeleted("0");
            this.save(statistic);
        } else {
            statistic.setWorkDays(workDays);
            statistic.setAttendDays(attendDays);
            statistic.setLateDays(lateDays);
            statistic.setEarlyDays(earlyDays);
            statistic.setAbsenceDays(absenceDays);
            this.updateById(statistic);
        }
    }
}