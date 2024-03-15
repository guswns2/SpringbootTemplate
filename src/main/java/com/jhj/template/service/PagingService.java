package com.jhj.template.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.template.dto.Test;
import com.jhj.template.mapper.PagingMapper;

@Service
public class PagingService implements PagingMapper {

	@Autowired
	PagingMapper pagingMapper;

	@Override
	public List<Test> pagingTest(HashMap<String, Object> param) {
		List<Test> res = pagingMapper.pagingTest(param);
		return res;
	}

	@Override
	public List<Object> pagingTest2(HashMap<String, Object> param) {
		List<Object> res = pagingMapper.pagingTest2(param);
		return res;
	}

}
