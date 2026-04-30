package com.oa.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.entity.AttendanceStatistic;

import java.util.List;

public interface AttendanceStatisticService extends IService<AttendanceStatistic> {

    Page<AttendanceStatistic> pageStatistics(Page<AttendanceStatistic> page, AttendanceStatistic statistic);

    List<AttendanceStatistic> getMyStatistics(Long userId, Integer year, Integer month);

    void generateMonthlyStatistics(Integer year, Integer month);
}