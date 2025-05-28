package com.jhj.template.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.jhj.template.dto.User;
import com.jhj.template.service.JwtService;
import com.jhj.template.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final LoginService loginService;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        String username = extractUsername(authentication); // 인증 정보에서 Username 추출

        String accessToken = "";
        String refreshToken = "";

        // 유저 회원가입 시에는 Redis에 refreshToken이 없음
        // 따라서, 로그인 성공 시 createRefreshToken에서 RefeshToken을 발급하고, Redis에 저장
        Optional<User> userOptional = loginService.findByUsername(username);
        if (userOptional.isPresent()) {
            // Redis에 저장 시
            accessToken = jwtService.createAccessToken(username); // AccessToken 발급
            refreshToken = jwtService.createRefreshToken(username); // RefreshToken 발급
            jwtService.setRefreshTokenRedis(username, refreshToken); // Redis에 저장
            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 헤더에 실어 전송
        }

        // DB 저장 시
        // Optional<User> user = loginService.findByUsername(username)
        // .ifPresent(user -> {
        // user.setRefreshToken(refreshToken);
        // loginMapper.saveAndFlush(user);
        // loginService.updateRefreshToken(user);
        // jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);})

        log.info("로그인에 성공하였습니다. 아이디 : {}", username);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
