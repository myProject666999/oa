package com.oa.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    User getByUsername(String username);

    List<String> getRoleCodes(Long userId);

    List<String> getPermissions(Long userId);

    Page<User> pageUsers(Page<User> page, User user);

    boolean resetPassword(Long userId, String password);

    boolean updateStatus(Long userId, String status);
}