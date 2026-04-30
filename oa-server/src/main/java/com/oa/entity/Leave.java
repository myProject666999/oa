package com.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("leave_apply")
public class Leave {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double leaveDays;
    private String reason;
    private String status;
    private Long approveBy;
    private String approveComment;
    private LocalDateTime approveTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String deleted;
}