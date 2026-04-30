package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oa.common.Result;
import com.oa.entity.Announcement;
import com.oa.security.LoginUser;
import com.oa.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('announcement:list')")
    public Result<Page<Announcement>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Announcement announcement) {
        Page<Announcement> page = new Page<>(current, size);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        if (announcement != null) {
            if (StringUtils.hasText(announcement.getTitle())) {
                wrapper.like(Announcement::getTitle, announcement.getTitle());
            }
            if (StringUtils.hasText(announcement.getType())) {
                wrapper.eq(Announcement::getType, announcement.getType());
            }
            if (StringUtils.hasText(announcement.getStatus())) {
                wrapper.eq(Announcement::getStatus, announcement.getStatus());
            }
        }
        wrapper.eq(Announcement::getDeleted, "0");
        wrapper.orderByDesc(Announcement::getCreateTime);
        Page<Announcement> result = announcementService.page(page, wrapper);
        return Result.success(result);
    }

    @GetMapping("/list")
    public Result<List<Announcement>> list() {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getStatus, "0");
        wrapper.eq(Announcement::getDeleted, "0");
        wrapper.orderByDesc(Announcement::getCreateTime);
        wrapper.last("LIMIT 10");
        List<Announcement> list = announcementService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Announcement> getById(@PathVariable Long id) {
        Announcement announcement = announcementService.getById(id);
        return Result.success(announcement);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('announcement:add')")
    public Result<Boolean> add(@RequestBody Announcement announcement, 
                               @AuthenticationPrincipal LoginUser loginUser) {
        announcement.setCreateBy(loginUser.getUser().getId());
        announcement.setDeleted("0");
        boolean result = announcementService.save(announcement);
        return Result.success(result);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('announcement:edit')")
    public Result<Boolean> update(@RequestBody Announcement announcement) {
        boolean result = announcementService.updateById(announcement);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('announcement:remove')")
    public Result<Boolean> delete(@PathVariable Long id) {
        Announcement announcement = new Announcement();
        announcement.setId(id);
        announcement.setDeleted("1");
        boolean result = announcementService.updateById(announcement);
        return Result.success(result);
    }
}