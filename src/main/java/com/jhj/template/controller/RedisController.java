package com.jhj.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.template.common.Constants.RESPONSE;
import com.jhj.template.dto.Response;
import com.jhj.template.dto.Test;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Redis API", description = "RedisController")
@Slf4j
@RestController
@RequestMapping("/api4")
public class RedisController {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/redisGet")
    public Response<Object> redisGet(@RequestParam(value = "key", required = false) String key) {
        Response<Object> res = new Response<Object>();

        try {
            Object result = redisTemplate.opsForValue().get(key);

            res.setData(result);
            res.setMessage("Redis get 성공");

        } catch (Exception e) {
            log.error("Redis get 실패", e);
            res.setRespone(RESPONSE.FAIL);
            res.setMessage("Redis get 실패");
        }

        return res;
    }

    @GetMapping("/redisSet")
    public Response<String> redisSet() {
        Response<String> res = new Response<String>();

        try {
            Test test = new Test();
            test.setIdx(1);
            test.setCol1("a");
            test.setCol2("b");

            redisTemplate.opsForValue().set("0", "test", 10, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set("1", test, 10, TimeUnit.MINUTES);

            res.setData("Redis set 성공");
        } catch (Exception e) {
            log.error("Redis set 실패", e);
            res.setData("Redis set 실패");
        }

        return res;
    }

    @GetMapping("/redisDelete")
    public Response<String> redisDelete(@RequestParam(value = "key", required = false) String key) {
        Response<String> res = new Response<String>();

        try {
            redisTemplate.delete(key);

            res.setData("Redis delete 성공");
        } catch (Exception e) {
            log.error("Redis delete 실패", e);
            res.setData("Redis delete 실패");
        }

        return res;
    }

}
