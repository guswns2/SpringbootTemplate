package com.jhj.template.mapper;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jhj.template.dto.Test;

@Repository
public interface PagingMapper {

	public List<Test> pagingTest(HashMap<String, Object> param);

	public List<Object> pagingTest2(HashMap<String, Object> param);

}
