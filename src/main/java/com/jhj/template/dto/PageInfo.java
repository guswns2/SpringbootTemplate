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

    public PageInfo() {}

    public PageInfo(int pageNum, int rowsPerPage, int totalCnt, List<T> totalDataList) {

        this.pageNum = pageNum;
        this.rowsPerPage = rowsPerPage;
        this.totalCnt = totalCnt;
        this.totalDataList = totalDataList;
    }

}
