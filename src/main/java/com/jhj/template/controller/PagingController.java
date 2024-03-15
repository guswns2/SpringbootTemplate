package com.jhj.template.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.template.dto.PageInfo;
import com.jhj.template.dto.Response;
import com.jhj.template.dto.Test;
import com.jhj.template.dto.TotalDataCnt;
import com.jhj.template.service.PagingService;
import com.jhj.template.utils.JsonUtils;
import com.jhj.template.utils.PagingSet;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Paging API", description = "PagingController")
@Slf4j
@RestController
@RequestMapping("/api3")
public class PagingController {

	@Autowired
	PagingService pagingService;

	// Paging 테스트
	@GetMapping(value = "/pagingTest")
	public Response<PageInfo<Test>> pagingTest(@RequestParam(value = "idx", required = false) Integer idx,
			@RequestParam(value = "col1", defaultValue = "") String col1,
			@RequestParam(value = "col2", defaultValue = "") String col2,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "rowsPerPage", defaultValue = "2") int rowsPerPage,
			@RequestParam(value = "isExcel", required = false, defaultValue = "N") String isExcel) {

		List<Test> resDataList = new ArrayList<Test>();

		Boolean isSucc = false;
		int pageNum2 = pageNum; // 요청한 페이지 번호
		int rowsPerPage2 = rowsPerPage; // 요청한 페이지 행 개수
		int maxRows = 1000;
		int totalDataCnt = 0; // 데이터 전체 개수

		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("idx", idx);
			param.put("col1", col1);
			param.put("col2", col2);

			// 엑셀 저장이면
			if (isExcel.equalsIgnoreCase("Y")) {
				param.put("startRow", 0);
				param.put("rowsPerPage", maxRows);
				resDataList = pagingService.pagingTest(param);
			} else {
				param.put("isTotalCnt", "Y");

				// List<Test> resDataCnt = restApiService.pagingTest(param);
				List<Object> resDataCnt = pagingService.pagingTest2(param);

				// api의 return이 HashMap일 때 DTO로 변환
				if (resDataCnt.get(0).getClass().getSimpleName().equals("HashMap")) {
					TotalDataCnt tdc = JsonUtils.getObjectToClass(resDataCnt.get(0), TotalDataCnt.class);
					resDataCnt.set(0, tdc);
				}

				HashMap<String, Object> pageParams = PagingSet.setPageParams(param, pageNum, rowsPerPage,
						maxRows, resDataCnt);
				if (pageParams.size() > 0) {
					pageNum2 = Integer.parseInt(pageParams.get("pageNum").toString());
					rowsPerPage2 = Integer.parseInt(pageParams.get("rowsPerPage").toString());
					totalDataCnt = Integer.parseInt(pageParams.get("totalCnt").toString());

					resDataList = pagingService.pagingTest(param);
				}

			}

			isSucc = true;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
		}

		return PagingSet.setResDataPage(isSucc, pageNum2, rowsPerPage2, totalDataCnt, resDataList);
	}
}
