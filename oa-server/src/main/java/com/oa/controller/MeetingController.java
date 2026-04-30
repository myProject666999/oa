package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oa.common.Result;
import com.oa.entity.Meeting;
import com.oa.security.LoginUser;
import com.oa.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @GetMapping("/list")
    public Result<List<Meeting>> list(Meeting meeting) {
        LambdaQueryWrapper<Meeting> wrapper = new LambdaQueryWrapper<>();
        if (meeting != null) {
            if (StringUtils.hasText(meeting.getMeetingName())) {
                wrapper.like(Meeting::getMeetingName, meeting.getMeetingName());
            }
            if (meeting.getRoomId() != null) {
                wrapper.eq(Meeting::getRoomId, meeting.getRoomId());
            }
        }
        wrapper.eq(Meeting::getDeleted, "0");
        wrapper.orderByDesc(Meeting::getStartTime);
        List<Meeting> list = meetingService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('meeting:list')")
    public Result<Page<Meeting>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Meeting meeting) {
        Page<Meeting> page = new Page<>(current, size);
        LambdaQueryWrapper<Meeting> wrapper = new LambdaQueryWrapper<>();
        if (meeting != null) {
            if (StringUtils.hasText(meeting.getMeetingName())) {
                wrapper.like(Meeting::getMeetingName, meeting.getMeetingName());
            }
            if (meeting.getRoomId() != null) {
                wrapper.eq(Meeting::getRoomId, meeting.getRoomId());
            }
            if (StringUtils.hasText(meeting.getStatus())) {
                wrapper.eq(Meeting::getStatus, meeting.getStatus());
            }
        }
        wrapper.eq(Meeting::getDeleted, "0");
        wrapper.orderByDesc(Meeting::getStartTime);
        Page<Meeting> result = meetingService.page(page, wrapper);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Meeting> getById(@PathVariable Long id) {
        Meeting meeting = meetingService.getById(id);
        return Result.success(meeting);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('meeting:add')")
    public Result<Boolean> add(@RequestBody Meeting meeting, 
                               @AuthenticationPrincipal LoginUser loginUser) {
        meeting.setOrganizer(loginUser.getUser().getRealName());
        meeting.setDeleted("0");
        boolean result = meetingService.save(meeting);
        return Result.success(result);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('meeting:edit')")
    public Result<Boolean> update(@RequestBody Meeting meeting) {
        boolean result = meetingService.updateById(meeting);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('meeting:remove')")
    public Result<Boolean> delete(@PathVariable Long id) {
        Meeting meeting = new Meeting();
        meeting.setId(id);
        meeting.setDeleted("1");
        boolean result = meetingService.updateById(meeting);
        return Result.success(result);
    }
}