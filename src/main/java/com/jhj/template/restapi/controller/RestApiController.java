package com.jhj.template.restapi.controller;

import java.util.HashMap;
import java.util.List;

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

@Tag(name = "Rest API", description = "RestApiController")
@SecurityRequirement(name = "Authorization") // 설정해줘야 swagger ui에서 헤더에 name값으로 key가 들어감
@Slf4j
@RestController
@RequestMapping("/api1")
public class RestApiController {

    @Autowired
    RestApiService restApiService;

    // @Mapper 사용 시
    // private RestApiServiceImpl restApiServiceImpl;
    // public RestApiServiceController(RestApiServiceImpl restApiServiceImpl) {
    // this.RestApiServiceImpl = restApiServiceImpl;
    // }

    //////////////////////////////////////////////////////////////////////////////////////////////

    // Restful HTTP API 메서드 테스트
    // GET 방식
    @Operation(summary = "getTest", description = "GET METHOD TEST", parameters = {
            @Parameter(name = "idx", description = "인덱스", example = "0", in = ParameterIn.QUERY),
            @Parameter(name = "col1", description = "컬럼1", example = "", in = ParameterIn.QUERY),
            @Parameter(name = "col2", description = "컬럼2", example = "", in = ParameterIn.QUERY)},
            responses = {@ApiResponse(responseCode = "200", description = "Successful response"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")}
    // , security = @SecurityRequirement(name = "Authorization") // 인증 부분적용
    )
    @GetMapping(value = "/getTest")
    public Response<Test> getTest(@RequestParam(value = "idx", required = false) Integer idx,
            @RequestParam(value = "col1", defaultValue = "") String col1,
            @RequestParam(value = "col2", defaultValue = "") String col2) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        Response<Test> res = new Response<Test>();

        try {
            param.put("idx", idx);
            param.put("col1", col1);
            param.put("col2", col2);
            List<Test> result = restApiService.getTest(param);
            // List<Test> result = restApiServiceImpl.getTest(param);

            res.setDataList(result);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
            res.setRespone(RESPONSE.FAIL);
        }

        return res;
    }

    @GetMapping(value = "/getTest2")
    public Response<Object> getTest2(@RequestParam(value = "idx", required = false) Integer idx,
            @RequestParam(value = "col1", defaultValue = "") String col1,
            @RequestParam(value = "col2", defaultValue = "") String col2) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        Response<Object> res = new Response<Object>();

        try {
            param.put("idx", idx);
            param.put("col1", col1);
            param.put("col2", col2);
            List<Object> result = restApiService.getTest2(param);
            // List<Test> result = restApiServiceImpl.getTest(param);

            res.setDataList(result);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
            res.setRespone(RESPONSE.FAIL);
        }

        return res;
    }

    // HEAD 방식
    // 응답으로 헤더만 리턴
    @Operation(summary = "headTest", description = "HEAD METHOD TEST")
    @RequestMapping(value = "/headTest", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headTest(@RequestHeader(required = false) HttpHeaders header) {

        header.add("a", "aa");

        return new ResponseEntity<>(header, HttpStatus.OK);
    }

    // POST 방식
    @Operation(summary = "postTest", description = "POST METHOD TEST",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Test 객체를 담은 파라미터",
                    content = @Content(examples = {@ExampleObject(name = "Test Model", value = """
                                {
                                  "idx" : "1",
                                  "col1" : "a",
                                  "col2" : "b"
                                }
                            """)})),
            responses = {@ApiResponse(responseCode = "200", description = "Successful response"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")})
    @PostMapping(value = "/postTest")
    public Response<Integer> postTest(@RequestBody(required = true) Request<Test> req) {

        Response<Integer> res = new Response<Integer>();

        try {
            Test test = req.getParam();
            int result = restApiService.postTest(test);
            if (result < 1) {
                res.setRespone(RESPONSE.FAIL);
            }
            res.setData(result);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
            res.setRespone(RESPONSE.FAIL);
        }

        return res;
    }

    // PUT 방식
    // 존재하는 리소스를 전체적으로 update 할 때 사용
    @Operation(summary = "putTest", description = "PUT METHOD TEST")
    @PutMapping(value = "/putTest")
    public Response<Integer> putTest(@RequestBody(required = true) Request<Test> req) {

        Response<Integer> res = new Response<Integer>();

        try {
            Test test = req.getParam();
            int result = restApiService.putTest(test);
            if (result < 1) {
                res.setRespone(RESPONSE.FAIL);
            }
            res.setData(result);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
            res.setRespone(RESPONSE.FAIL);
        }

        return res;
    }

    // PATCH 방식
    @Operation(summary = "patchTest", description = "PATCH METHOD TEST")
    @PatchMapping(value = "/patchTest")
    public Response<Integer> patchTest(@RequestParam(value = "idx") Integer idx, // int는 null 허용 X
            @RequestParam(value = "col1", defaultValue = "") String col1,
            @RequestParam(value = "col2", defaultValue = "") String col2) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        Response<Integer> res = new Response<Integer>();

        try {
            param.put("idx", idx);
            param.put("col1", col1);
            param.put("col2", col2);
            int result = restApiService.patchTest(param);
            if (result < 1) {
                res.setRespone(RESPONSE.FAIL);
            }
            res.setData(result);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
            res.setRespone(RESPONSE.FAIL);
        }

        return res;
    }

    // DELETE 방식
    @Operation(summary = "deleteTest", description = "DELETE METHOD TEST")
    @DeleteMapping(value = "/deleteTest")
    public Response<Integer> deleteTest(@RequestParam(value = "idx", required = false) Integer idx, // int는
                                                                                                    // null
                                                                                                    // 허용
                                                                                                    // X
            @RequestParam(value = "col1", defaultValue = "") String col1,
            @RequestParam(value = "col2", defaultValue = "") String col2) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        Response<Integer> res = new Response<Integer>();

        try {
            param.put("idx", idx);
            param.put("col1", col1);
            param.put("col2", col2);
            int result = restApiService.deleteTest(param);
            if (result < 1) {
                res.setRespone(Constants.RESPONSE.FAIL);
            }
            res.setData(result);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
            res.setRespone(Constants.RESPONSE.FAIL);
        }

        return res;
    }

    // OPTIONS 방식
    // 응답 헤더에서 허영된 메서드 종류 확인 가능
    @Operation(summary = "optionsTest", description = "OPTIONS METHOD TEST")
    @RequestMapping(value = "/optionsTest", method = RequestMethod.OPTIONS)
    public void optionsTest() {
        log.info("options");
    }

}
