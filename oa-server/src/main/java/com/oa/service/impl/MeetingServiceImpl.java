package com.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.entity.Meeting;
import com.oa.mapper.MeetingMapper;
import com.oa.service.MeetingService;
import org.springframework.stereotype.Service;

@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting> implements MeetingService {
}
