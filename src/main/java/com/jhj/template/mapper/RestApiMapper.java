package com.jhj.template.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.jhj.template.dto.Test;

@Repository
public interface RestApiMapper {

	public List<Test> getTest(HashMap<String, Object> param);

	public List<Object> getTest2(HashMap<String, Object> param);

	public int postTest(Test test);

	public int putTest(Test test);

	public int patchTest(HashMap<String, Object> param);

	public int deleteTest(HashMap<String, Object> param);

}

// @Mapper
// public interface RestApiMapper {

// @Select("<script>" +
// " select idx, col1, col2 from test where 1 = 1" +
// " <if test='idx != null and idx != 0'>" +
// " and idx = #{idx}" +
// " </if>" +
// " <if test='col1 != null and !col1.equals(\"\")'>" +
// " and col1 = #{col1}" +
// " </if>" +
// " <if test='col2 != null and !col2.equals(\"\")'>" +
// " and col2 = #{col2}" +
// " </if>" +
// "</script>")
// public List<Test> getTest(HashMap<String, Object> param);

// public int postTest(HashMap<String, Object> param);

// public int putTest(HashMap<String, Object> param);

// public int deleteTest(HashMap<String, Object> param);

// }