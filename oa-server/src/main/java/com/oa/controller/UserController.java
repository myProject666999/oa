package com.oa.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oa.common.Result;
import com.oa.entity.User;
import com.oa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<Page<User>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            User user) {
        Page<User> page = new Page<>(current, size);
        Page<User> result = userService.pageUsers(page, user);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<Boolean> add(@RequestBody User user) {
        User exist = userService.getByUsername(user.getUsername());
        if (exist != null) {
            return Result.error("用户名已存在");
        }
        user.setPassword(null);
        user.setDeleted("0");
        boolean result = userService.save(user);
        return Result.success(result);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Boolean> update(@RequestBody User user) {
        user.setUsername(null);
        user.setPassword(null);
        boolean result = userService.updateById(user);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public Result<Boolean> delete(@PathVariable Long id) {
        User user = new User();
        user.setId(id);
        user.setDeleted("1");
        boolean result = userService.updateById(user);
        return Result.success(result);
    }

    @PutMapping("/resetPwd")
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    public Result<Boolean> resetPassword(@RequestBody User user) {
        boolean result = userService.resetPassword(user.getId(), "123456");
        return Result.success(result);
    }

    @PutMapping("/changeStatus")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Boolean> changeStatus(@RequestBody User user) {
        boolean result = userService.updateStatus(user.getId(), user.getStatus());
        return Result.success(result);
    }
}