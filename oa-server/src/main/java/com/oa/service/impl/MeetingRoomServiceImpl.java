package com.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.entity.MeetingRoom;
import com.oa.mapper.MeetingRoomMapper;
import com.oa.service.MeetingRoomService;
import org.springframework.stereotype.Service;

@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements MeetingRoomService {
}
