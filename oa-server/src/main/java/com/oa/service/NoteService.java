package com.oa.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.entity.Note;

public interface NoteService extends IService<Note> {

    Page<Note> getMyNotes(Page<Note> page, Long userId);
}
