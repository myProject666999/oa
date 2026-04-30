package com.oa.task;

import com.oa.service.AttendanceStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AttendanceStatisticTask {

    @Autowired
    private AttendanceStatisticService attendanceStatisticService;

    @Scheduled(cron = "0 0 2 1 * ?")
    public void generateMonthlyStatistics() {
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);
        int year = lastMonth.getYear();
        int month = lastMonth.getMonthValue();
        
        attendanceStatisticService.generateMonthlyStatistics(year, month);
    }
}