package com.jhj.template.dto;

import java.util.List;

import com.jhj.template.common.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class Response<T> {
	private String code = "200";
	private String message = "SUCC";

	private T data;
	private List<T> dataList;

	public void setRespone(Constants.RESPONSE res) {
		this.code = res.getCode();
		this.message = res.getMessage();
	}
}
