package com.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.entity.Note;
import com.oa.mapper.NoteMapper;
import com.oa.service.NoteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

    @Override
    public Page<Note> getMyNotes(Page<Note> page, Long userId) {
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Note::getUserId, userId);
        wrapper.eq(Note::getDeleted, "0");
        wrapper.orderByDesc(Note::getCreateTime);
        return this.page(page, wrapper);
    }

    public Note saveNote(Note note, Long userId) {
        note.setUserId(userId);
        note.setCreateTime(LocalDateTime.now());
        note.setUpdateTime(LocalDateTime.now());
        note.setDeleted("0");
        this.save(note);
        return note;
    }

    public Note updateNote(Note note) {
        note.setUpdateTime(LocalDateTime.now());
        this.updateById(note);
        return note;
    }
}
