package com.jhj.template.notice.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.jhj.template.notice.model.Notice;

@Mapper
public interface NoticeMapper {

    public List<Notice> getNoticeList(Map<String, Object> paramsMap);

}
