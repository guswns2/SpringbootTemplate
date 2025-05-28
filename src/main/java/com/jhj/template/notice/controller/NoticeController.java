package com.jhj.template.notice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.template.common.Constants;
import com.jhj.template.common.Constants.RESPONSE;
import com.jhj.template.common.model.response.Request;
import com.jhj.template.common.model.response.Response;
import com.jhj.template.common.model.test.Test;
import com.jhj.template.notice.model.Notice;
import com.jhj.template.notice.service.NoticeService;
import com.jhj.template.restapi.service.RestApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Notice API", description = "NoticeController")
@SecurityRequirement(name = "Authorization") // 설정해줘야 swagger ui에서 헤더에 name값으로 key가 들어감
@Slf4j
@RestController
@RequestMapping("/api9")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @Operation(summary = "getNoticeList", description = "게시글 목록 조회", parameters = {
            @Parameter(name = "title", description = "제목", example = "", in = ParameterIn.QUERY),
            @Parameter(name = "userId", description = "작성자", example = "", in = ParameterIn.QUERY)},
            responses = {@ApiResponse(responseCode = "200", description = "Successful response"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")}
    // , security = @SecurityRequirement(name = "Authorization") // 인증 부분적용
    )
    @GetMapping(value = "/getNoticeList")
    public Response<Notice> getNoticeList(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "userId", defaultValue = "") String userId) {

        Map<String, Object> param = new HashMap<String, Object>();
        Response<Notice> res = new Response<Notice>();

        try {
            param.put("title", title);
            param.put("userId", userId);

            List<Notice> result = noticeService.getNoticeList(param);

            res.setDataList(result);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
            res.setRespone(RESPONSE.FAIL);
        }

        return res;
    }

    // @RequestMapping(value = "getNoticeList", method = { RequestMethod.GET }, produces =
    // "application/json; charset=utf8")
    // // @ResponseBody
    // public ListDataResponse<List<Notice>> getNoticeList(@RequestParam(name = "title", required =
    // false, defaultValue = "") String title,
    // @RequestParam(name = "userId", required = false, defaultValue = "") String userId) {

    // // String token = (String) session.getAttribute(DashboardConstant.SESSION_TOKEN_KEY);
    // // LoginInfo loginInfo = cache.getLoginInfo(token);
    // // String authId = loginInfo.getAuthId();

    // Map<String, Object> paramsMap = new HashMap<String, Object>();
    // paramsMap.put("title", title);
    // paramsMap.put("userId", userId);

    // // if (!Strings.isNullOrEmpty(sortBy)) {
    // // paramMap.put("sortBy", sortBy);
    // // }
    // // if (!Strings.isNullOrEmpty(sensorId)) {
    // // paramMap.put("sensorId", sensorId);
    // // }
    // // if (snsrIdList != null && snsrIdList.size() > 0) {
    // // paramMap.put("snsrIdList", snsrIdList);
    // // }

    // ListDataResponse<List<Notice>> response = new ListDataResponse<List<Notice>>();
    // try {
    // int totalCount = eventService.getNoticeListCount(paramsMap);

    // PageInfo pageInfo = new PageInfo(String.valueOf(totalCount));

    // if (isExcel) {
    // if (totalCount > accpetRowCnt) {
    // response.setResponseCode(DashboardConstant.RESPONSE.OVER_EXCEL_ROWS.getCode());
    // response.setResponseMessage(accpetRowCnt +
    // DashboardConstant.RESPONSE.OVER_EXCEL_ROWS.getMessage());
    // paramMap.put("limit", accpetRowCnt);
    // } else {
    // response.setResponseCode(DashboardConstant.RESPONSE.SUCCESS.getCode());
    // response.setResponseMessage(DashboardConstant.RESPONSE.SUCCESS.getMessage());
    // }
    // } else {
    // response.setResponseCode(DashboardConstant.RESPONSE.SUCCESS.getCode());
    // response.setResponseMessage(DashboardConstant.RESPONSE.SUCCESS.getMessage());
    // }

    // List<Event> result = eventService.getAllEventList(paramMap);

    // response.setData(result);
    // response.setPageInfo(pageInfo);

    // } catch (Exception e) {
    // e.printStackTrace();
    // response.setResponseCode(DashboardConstant.RESPONSE.FAIL.getCode());
    // response.setResponseMessage(DashboardConstant.RESPONSE.FAIL.getMessage());
    // }

    // return response;
    // }

}
