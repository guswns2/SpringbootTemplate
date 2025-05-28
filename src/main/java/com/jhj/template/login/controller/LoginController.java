package com.jhj.template.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;
import com.jhj.template.common.model.response.Request;
import com.jhj.template.common.model.response.Response;
import com.jhj.template.login.model.User;
import com.jhj.template.login.service.JwtService;
import com.jhj.template.login.service.UserService;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Login API", description = "LoginController")
@SecurityRequirement(name = "Authorization") // 설정해줘야 swagger ui에서 헤더에 name값으로 key가 들어감
@Slf4j
@RestController
@RequestMapping("/api2")
public class LoginController {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @GetMapping(value = "/getAuthorization")
    public String getAuthorization(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        // request header에서 value값으로 된 key값 받아옴
        log.info("auth token is " + authorization);

        if (authorization == null) {
            return "not allowed.. token : " + authorization;
        }

        return "authenticated! token : " + authorization;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody Request<User> req) {
        User user = req.getParam();

        try {
            userService.signup(user);

            return "회원가입 성공";

        } catch (Exception e) {
            return "회원가입 실패";
        }
    }

    @Operation(summary = "login", description = "LOGIN TEST",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "로그인 요청",
                    content = @Content(examples = {@ExampleObject(name = "id password", value = """
                            		{
                            			"username" : "admin",
                            			"password" : "admin"
                            		}
                            """)})),
            responses = {})
    @PostMapping("/login")
    public void login(@RequestBody Request req) {}

    @PostMapping("/logout")
    public Response<String> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Response<String> res = new Response<String>();
        try {
            String accessToken = authorization.replace("Bearer ", "");

            // redis에서 username을 키로 가진 refreshToken 삭제
            String username = jwtService.extractUsername(accessToken).orElseThrow(
                    () -> new NoSuchElementException("username is not present in accessToken"));
            redisTemplate.delete(username);

            // redis에 accessToken 값을 키로 가진 로그아웃 상태를 추가
            long expiration = jwtService.getExpiration(accessToken);
            redisTemplate.opsForValue().set(accessToken, "logout", expiration,
                    TimeUnit.MILLISECONDS);

            res.setMessage("로그아웃 성공");
        } catch (Exception e) {
            log.error("로그아웃 실패", e);
            res.setMessage("로그아웃 실패");
        }
        return res;
    }

}
