package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oa.common.Result;
import com.oa.entity.MeetingRoom;
import com.oa.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meeting-room")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping("/list")
    public Result<List<MeetingRoom>> list(MeetingRoom room) {
        LambdaQueryWrapper<MeetingRoom> wrapper = new LambdaQueryWrapper<>();
        if (room != null) {
            if (StringUtils.hasText(room.getRoomName())) {
                wrapper.like(MeetingRoom::getRoomName, room.getRoomName());
            }
            if (StringUtils.hasText(room.getStatus())) {
                wrapper.eq(MeetingRoom::getStatus, room.getStatus());
            }
        }
        wrapper.eq(MeetingRoom::getDeleted, "0");
        wrapper.orderByAsc(MeetingRoom::getId);
        List<MeetingRoom> list = meetingRoomService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('meeting:room:list')")
    public Result<Page<MeetingRoom>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            MeetingRoom room) {
        Page<MeetingRoom> page = new Page<>(current, size);
        LambdaQueryWrapper<MeetingRoom> wrapper = new LambdaQueryWrapper<>();
        if (room != null) {
            if (StringUtils.hasText(room.getRoomName())) {
                wrapper.like(MeetingRoom::getRoomName, room.getRoomName());
            }
            if (StringUtils.hasText(room.getStatus())) {
                wrapper.eq(MeetingRoom::getStatus, room.getStatus());
            }
        }
        wrapper.eq(MeetingRoom::getDeleted, "0");
        wrapper.orderByAsc(MeetingRoom::getId);
        Page<MeetingRoom> result = meetingRoomService.page(page, wrapper);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('meeting:room:query')")
    public Result<MeetingRoom> getById(@PathVariable Long id) {
        MeetingRoom room = meetingRoomService.getById(id);
        return Result.success(room);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('meeting:room:add')")
    public Result<Boolean> add(@RequestBody MeetingRoom room) {
        room.setDeleted("0");
        boolean result = meetingRoomService.save(room);
        return Result.success(result);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('meeting:room:edit')")
    public Result<Boolean> update(@RequestBody MeetingRoom room) {
        boolean result = meetingRoomService.updateById(room);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('meeting:room:remove')")
    public Result<Boolean> delete(@PathVariable Long id) {
        MeetingRoom room = new MeetingRoom();
        room.setId(id);
        room.setDeleted("1");
        boolean result = meetingRoomService.updateById(room);
        return Result.success(result);
    }
}