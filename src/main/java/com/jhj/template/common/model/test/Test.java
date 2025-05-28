package com.jhj.template.common.model.test;

import com.jhj.template.common.model.response.TotalDataCnt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Test extends TotalDataCnt {

    @Schema(description = "인덱스")
    private Integer idx;

    private String col1, col2;

}
