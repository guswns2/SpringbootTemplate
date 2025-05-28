package com.jhj.template.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TotalDataCnt {

    @JsonIgnore
    private int totalDataCnt;

}
