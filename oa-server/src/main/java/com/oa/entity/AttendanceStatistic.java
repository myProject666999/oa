package com.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("attendance_statistic")
public class AttendanceStatistic {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer year;
    private Integer month;
    private Integer workDays;
    private Integer attendDays;
    private Integer lateDays;
    private Integer earlyDays;
    private Integer absenceDays;
    private Integer leaveDays;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String deleted;
}