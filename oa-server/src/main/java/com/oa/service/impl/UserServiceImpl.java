package com.oa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.entity.User;
import com.oa.mapper.UserMapper;
import com.oa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, "0"));
    }

    @Override
    public List<String> getRoleCodes(Long userId) {
        return baseMapper.selectRoleCodesByUserId(userId);
    }

    @Override
    public List<String> getPermissions(Long userId) {
        return baseMapper.selectPermsByUserId(userId);
    }

    @Override
    public Page<User> pageUsers(Page<User> page, User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (user != null) {
            if (StringUtils.hasText(user.getUsername())) {
                wrapper.like(User::getUsername, user.getUsername());
            }
            if (StringUtils.hasText(user.getRealName())) {
                wrapper.like(User::getRealName, user.getRealName());
            }
            if (StringUtils.hasText(user.getStatus())) {
                wrapper.eq(User::getStatus, user.getStatus());
            }
            if (user.getDeptId() != null) {
                wrapper.eq(User::getDeptId, user.getDeptId());
            }
        }
        wrapper.eq(User::getDeleted, "0");
        wrapper.orderByDesc(User::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public boolean resetPassword(Long userId, String password) {
        User user = new User();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(password));
        return this.updateById(user);
    }

    @Override
    public boolean updateStatus(Long userId, String status) {
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        return this.updateById(user);
    }
}