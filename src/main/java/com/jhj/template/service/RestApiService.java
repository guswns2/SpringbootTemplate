package com.jhj.template.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.template.dto.Test;
import com.jhj.template.mapper.RestApiMapper;

@Service
public class RestApiService implements RestApiMapper {

    @Autowired
    RestApiMapper restApiMapper;

    @Override
    public List<Test> getTest(HashMap<String, Object> param) {
        List<Test> res = restApiMapper.getTest(param);
        return res;
    }

    @Override
    public List<Object> getTest2(HashMap<String, Object> param) {
        List<Object> res = restApiMapper.getTest2(param);
        return res;
    }

    @Override
    public int postTest(Test test) {
        int res = restApiMapper.postTest(test);
        return res;
    }

    @Override
    public int putTest(Test test) {
        int res = restApiMapper.putTest(test);
        return res;
    }

    @Override
    public int patchTest(HashMap<String, Object> param) {
        int res = restApiMapper.patchTest(param);
        return res;
    }

    @Override
    public int deleteTest(HashMap<String, Object> param) {
        int res = restApiMapper.deleteTest(param);
        return res;
    }

}

// @Mapper 사용 시
// public interface RestApiService {

// public List<Test> getTest(HashMap<String, Object> param);

// public int postTest(HashMap<String, Object> param);

// public int putTest(HashMap<String, Object> param);

// public int deleteTest(HashMap<String, Object> param);

// }
