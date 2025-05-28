package com.jhj.template.cache.mapper;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.jhj.template.common.model.test.Test;

@Mapper
public interface CacheMapper {

    public List<Test> getCacheAll();

    public List<Test> getCacheOne(HashMap<String, Object> param);

    public int updateCache(Test test);

    public int deleteCache(HashMap<String, Object> param);

}
