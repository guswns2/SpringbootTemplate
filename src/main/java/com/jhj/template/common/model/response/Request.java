package com.jhj.template.common.model.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
// @JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Request<T> {
    // @JsonProperty("test") // Json으로 받을 때 param을 'test'라는 이름으로 받는다
    @Schema(description = "객체 파라미터") // Swagger Schema 설명 적용이 안 됨..
    private T param;

    private List<T> paramList;
}
