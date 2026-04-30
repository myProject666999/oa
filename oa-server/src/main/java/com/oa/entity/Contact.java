package com.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_contact")
public class Contact {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String realName;
    private String phone;
    private String email;
    private Long deptId;
    private String deptName;
    private Long postId;
    private String postName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String deleted;
}