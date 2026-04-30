package com.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("sys_dept")
public class Department {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parentId;
    private String deptName;
    private String leader;
    private String phone;
    private String email;
    private Integer orderNum;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String deleted;

    @TableField(exist = false)
    private List<Department> children;
}