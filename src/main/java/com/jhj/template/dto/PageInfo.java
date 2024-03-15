/*
 * NDCS version 1.0
 *
 *  Copyright ⓒ 2023 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 */

package com.jhj.template.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PageInfo<T> implements Serializable {
	private static final long serialVersionUID = 8466848834078318086L;

	private int pageNum;
	private int rowsPerPage;
	private int totalCnt;
	private List<T> totalDataList;

	public PageInfo() {
	}

	public PageInfo(int pageNum, int rowsPerPage, int totalCnt, List<T> totalDataList) {

		this.pageNum = pageNum;
		this.rowsPerPage = rowsPerPage;
		this.totalCnt = totalCnt;
		this.totalDataList = totalDataList;
	}

}
