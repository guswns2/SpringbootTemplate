package com.jhj.template.mapper;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jhj.template.dto.Test;

@Repository
public interface CacheMapper {

	public List<Test> getCacheAll();

	public List<Test> getCacheOne(HashMap<String, Object> param);

	public int updateCache(Test test);

	public int deleteCache(HashMap<String, Object> param);

}
