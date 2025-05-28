package com.jhj.template.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.template.common.Constants;
import com.jhj.template.common.Constants.RESPONSE;
import com.jhj.template.dto.Request;
import com.jhj.template.dto.Response;
import com.jhj.template.dto.Test;
import com.jhj.template.service.CacheService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Cache API", description = "CacheController")
@Slf4j
@RestController
@RequestMapping("/api7")
public class CacheController {

    @Autowired
    CacheService cacheService;

    // SpringBoot 내장 캐시 테스트
    @Autowired
    CacheManager cacheManager;

    @GetMapping(value = "/getCacheAll")
    public Response<Test> getCacheAll() {

        Response<Test> res = new Response<Test>();

        try {
            List<Test> result = cacheService.getCacheAll();

            res.setDataList(result);

            // 캐시데이터 확인
            log.debug("getCacheAllgetCacheAllgetCacheAll");
            Collection<String> cacheNames = cacheManager.getCacheNames();
            log.debug(cacheNames + "");

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setRespone(RESPONSE.FAIL);
        }

        return res;
    }

    @GetMapping(value = "/getCacheOne")
    public Response<Test> getCacheOne(@RequestParam(value = "idx", required = false) Integer idx,
            @RequestParam(value = "col1", defaultValue = "") String col1,
            @RequestParam(value = "col2", defaultValue = "") String col2) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        Response<Test> res = new Response<Test>();

        try {
            param.put("idx", idx);
            param.put("col1", col1);
            param.put("col2", col2);
            List<Test> result = cacheService.getCacheOne(param);

            res.setDataList(result);

            // 캐시데이터 확인
            log.debug("getCacheOnegetCacheOnegetCacheOne");
            Collection<String> cacheNames = cacheManager.getCacheNames();
            log.debug(cacheNames + "");

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setRespone(RESPONSE.FAIL);
        }

        return res;
    }

    @PutMapping(value = "/updateCache")
    public Response<Test> updateCache(@RequestBody(required = true) Request<Test> req) {

        Response<Test> res = new Response<Test>();

        try {
            Test test = req.getParam();
            List<Test> result = cacheService.updateCache(test);

            res.setDataList(result);
            res.setMessage("DB UPDATE SUCCESS");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setRespone(RESPONSE.FAIL);
        }

        return res;
    }

    @DeleteMapping(value = "/deleteCache")
    public Response<Integer> deleteCache(@RequestParam(value = "idx", required = false) Integer idx, // int는
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
            int result = cacheService.deleteCache(param);
            if (result < 1) {
                res.setRespone(Constants.RESPONSE.FAIL);
            }
            res.setData(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setRespone(Constants.RESPONSE.FAIL);
        }

        return res;
    }

}
