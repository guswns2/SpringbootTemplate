package com.jhj.template.paging.mapper;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.jhj.template.common.model.test.Test;

@Mapper
public interface PagingMapper {

    public List<Test> pagingTest(HashMap<String, Object> param);

    public List<Object> pagingTest2(HashMap<String, Object> param);

}
