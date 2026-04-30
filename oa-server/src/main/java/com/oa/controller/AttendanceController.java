package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oa.common.Result;
import com.oa.entity.Attendance;
import com.oa.entity.AttendanceStatistic;
import com.oa.security.LoginUser;
import com.oa.service.AttendanceService;
import com.oa.service.AttendanceStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceStatisticService attendanceStatisticService;

    @PostMapping("/checkIn")
    public Result<Attendance> checkIn(@AuthenticationPrincipal LoginUser loginUser) {
        Attendance attendance = attendanceService.checkIn(loginUser.getUser().getId());
        return Result.success(attendance);
    }

    @PostMapping("/checkOut")
    public Result<Attendance> checkOut(@AuthenticationPrincipal LoginUser loginUser) {
        Attendance attendance = attendanceService.checkOut(loginUser.getUser().getId());
        return Result.success(attendance);
    }

    @GetMapping("/today")
    public Result<Attendance> getToday(@AuthenticationPrincipal LoginUser loginUser) {
        Attendance attendance = attendanceService.getTodayRecord(loginUser.getUser().getId());
        return Result.success(attendance);
    }

    @GetMapping("/myRecords")
    public Result<Page<Attendance>> myRecords(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Attendance attendance) {
        Page<Attendance> page = new Page<>(current, size);
        Page<Attendance> result = attendanceService.getMyRecords(page, loginUser.getUser().getId(), attendance);
        return Result.success(result);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('attendance:list')")
    public Result<Page<Attendance>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Attendance attendance) {
        Page<Attendance> page = new Page<>(current, size);
        Page<Attendance> result = attendanceService.pageRecords(page, attendance);
        return Result.success(result);
    }

    @GetMapping("/statistics/page")
    @PreAuthorize("hasAuthority('attendance:statistics')")
    public Result<Page<AttendanceStatistic>> statisticsPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            AttendanceStatistic statistic) {
        Page<AttendanceStatistic> page = new Page<>(current, size);
        Page<AttendanceStatistic> result = attendanceStatisticService.pageStatistics(page, statistic);
        return Result.success(result);
    }

    @GetMapping("/myStatistics")
    public Result<List<AttendanceStatistic>> myStatistics(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        List<AttendanceStatistic> list = attendanceStatisticService.getMyStatistics(loginUser.getUser().getId(), year, month);
        return Result.success(list);
    }
}