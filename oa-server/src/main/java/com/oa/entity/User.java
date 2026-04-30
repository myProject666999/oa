package com.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String avatar;
    private String phone;
    private String email;
    private Long deptId;
    private Long postId;
    private String status;
    private String gender;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String deleted;
}