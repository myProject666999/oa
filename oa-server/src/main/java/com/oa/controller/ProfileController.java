package com.oa.controller;

import com.oa.common.Result;
import com.oa.entity.User;
import com.oa.security.LoginUser;
import com.oa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public Result<User> getProfile(@AuthenticationPrincipal LoginUser loginUser) {
        User user = userService.getById(loginUser.getUser().getId());
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @PutMapping
    public Result<Boolean> updateProfile(@AuthenticationPrincipal LoginUser loginUser,
                                          @RequestBody User user) {
        user.setId(loginUser.getUser().getId());
        user.setUsername(null);
        user.setPassword(null);
        user.setStatus(null);
        boolean result = userService.updateById(user);
        return Result.success(result);
    }

    @PutMapping("/password")
    public Result<Boolean> updatePassword(@AuthenticationPrincipal LoginUser loginUser,
                                           @RequestParam String oldPassword,
                                           @RequestParam String newPassword) {
        User user = userService.getById(loginUser.getUser().getId());
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.error("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        boolean result = userService.updateById(user);
        return Result.success(result);
    }
}
