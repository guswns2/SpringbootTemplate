package com.jhj.template.utils;

import java.util.HashMap;
import java.util.List;

import com.jhj.template.common.Constants;
import com.jhj.template.dto.PageInfo;
import com.jhj.template.dto.Response;
import com.jhj.template.dto.TotalDataCnt;

public class PagingSet {

	// 페이징 파라미터 설정
	public static <T> HashMap<String, Object> setPageParams(
			HashMap<String, Object> param,
			int pageNum,
			int rowsPerPage,
			int maxRows,
			List<T> totalDataCnt) {

		HashMap<String, Object> hashMap = new HashMap<>();

		if (totalDataCnt.size() > 0) {
			int totalCount = ((TotalDataCnt) totalDataCnt.get(0)).getTotalDataCnt();

			if (totalCount > 0) {
				int pageNum2 = pageNum;
				int rowsPerPage2 = rowsPerPage;

				// 페이지당 행 최대 개수 설정
				if (rowsPerPage2 > maxRows) {
					rowsPerPage2 = maxRows;
				}

				// 전체 페이지 수
				int pageTotal = (int) (totalCount / rowsPerPage2);
				if ((totalCount % rowsPerPage2) > 0) {
					pageTotal++;
				}

				// 전체 페이지보다 요청 페이지가 크면 마지막 페이지로 설정
				if (pageNum2 > pageTotal) {
					pageNum2 = pageTotal;
				}

				int startRow = (pageNum2 - 1) * rowsPerPage2;
				hashMap.put("totalCnt", totalCount);
				hashMap.put("pageNum", pageNum2);
				hashMap.put("rowsPerPage", rowsPerPage2);

				param.remove("isTotalCnt");
				param.put("startRow", startRow);
				param.put("rowsPerPage", rowsPerPage2);
			}
		}

		return hashMap;
	}

	// 페이징 결과
	public static <T> Response<PageInfo<T>> setResDataPage(Boolean isSucc, int pageNum, int rowsPerPage, int totalDataCnt,
			List<T> dataList) {
		Response<PageInfo<T>> res = new Response<PageInfo<T>>();
		int pageNum2 = pageNum;

		if (isSucc) {
			int pageTotal = (int) (totalDataCnt / rowsPerPage); // 전체 페이지 수

			if ((totalDataCnt % rowsPerPage) > 0) {
				pageTotal++;
			}

			if (pageNum2 > pageTotal) {
				pageNum2 = pageTotal;
			}

			PageInfo<T> pageInfo = new PageInfo<T>(pageNum2, rowsPerPage, totalDataCnt, dataList);
			res.setData(pageInfo);
		} else {
			res.setRespone(Constants.RESPONSE.FAIL);
		}

		return res;
	}
}
