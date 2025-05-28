package com.jhj.template.notice.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jhj.template.notice.mapper.NoticeMapper;
import com.jhj.template.notice.model.Notice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticeService {

    @Autowired
    NoticeMapper noticeMapper;

    public List<Notice> getNoticeList(Map<String, Object> paramsMap) {
        List<Notice> result = noticeMapper.getNoticeList(paramsMap);
        return result;
    }

}
