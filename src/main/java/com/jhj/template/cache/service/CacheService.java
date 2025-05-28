package com.jhj.template.cache.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.jhj.template.cache.mapper.CacheMapper;
import com.jhj.template.common.model.test.Test;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CacheService {

    @Autowired
    CacheMapper cacheMapper;

    @Cacheable(value = "testAll", key = "1") // value: 캐시 저장소 이름, key: 캐시 데이터
    public List<Test> getCacheAll() {
        List<Test> res = cacheMapper.getCacheAll();
        log.debug("getCacheAll mapper 메서드 호출");
        return res;
    }

    @Cacheable(value = "test", key = "#param.get('idx')")
    public List<Test> getCacheOne(HashMap<String, Object> param) {
        List<Test> res = cacheMapper.getCacheOne(param);
        log.debug("getCacheOne mapper 메서드 호출");
        return res;
    }

    @CachePut(value = "test", key = "#test.idx")
    public List<Test> updateCache(Test test) {
        int res = cacheMapper.updateCache(test);

        List<Test> result = new ArrayList<Test>();
        if (res > 0) {
            result.add(test);
        }
        return result;
    }

    @CacheEvict(value = "test", allEntries = true)
    public int deleteCache(HashMap<String, Object> param) {
        int res = cacheMapper.deleteCache(param);
        return res;
    }
}
