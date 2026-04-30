package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oa.common.Result;
import com.oa.entity.Note;
import com.oa.security.LoginUser;
import com.oa.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/myList")
    public Result<Page<Note>> myList(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Page<Note> page = new Page<>(current, size);
        Page<Note> result = noteService.getMyNotes(page, loginUser.getUser().getId());
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Note> getById(@PathVariable Long id,
                                 @AuthenticationPrincipal LoginUser loginUser) {
        Note note = noteService.getById(id);
        if (note != null && !note.getUserId().equals(loginUser.getUser().getId())) {
            return Result.error("无权访问");
        }
        return Result.success(note);
    }

    @PostMapping
    public Result<Note> add(@RequestBody Note note,
                            @AuthenticationPrincipal LoginUser loginUser) {
        note.setUserId(loginUser.getUser().getId());
        note.setCreateTime(LocalDateTime.now());
        note.setUpdateTime(LocalDateTime.now());
        note.setDeleted("0");
        noteService.save(note);
        return Result.success(note);
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Note note,
                                   @AuthenticationPrincipal LoginUser loginUser) {
        Note exist = noteService.getById(note.getId());
        if (exist == null) {
            return Result.error("便签不存在");
        }
        if (!exist.getUserId().equals(loginUser.getUser().getId())) {
            return Result.error("只能修改自己的便签");
        }
        note.setUpdateTime(LocalDateTime.now());
        boolean result = noteService.updateById(note);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id,
                                  @AuthenticationPrincipal LoginUser loginUser) {
        Note exist = noteService.getById(id);
        if (exist == null) {
            return Result.error("便签不存在");
        }
        if (!exist.getUserId().equals(loginUser.getUser().getId())) {
            return Result.error("只能删除自己的便签");
        }
        exist.setDeleted("1");
        boolean result = noteService.updateById(exist);
        return Result.success(result);
    }
}
