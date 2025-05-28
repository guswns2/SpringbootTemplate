package com.jhj.template.common.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jhj.template.login.model.User;
import com.jhj.template.login.service.JwtService;
import com.jhj.template.login.service.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Jwt 인증 필터
// 클라이언트가 헤더에 JWT 토큰을 담아서 "/login" URL 이외의 요청을 보냈을 시,
// 해당 토큰들의 유효성을 검사하여 인증 처리/인증 실패/토큰 재발급 등을 수행하는 역할의 필터
// 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청
// AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AccessToken과 함께 요청

// 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다.
// 2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR
// 3. RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급(RTR 방식)
// 인증 성공 처리는 하지 않고 실패 처리
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    private static final String NO_CHECK_URL = "/api2/login"; // "/api2/login"으로 들어오는 요청은 Filter 작동
                                                              // X

    private final JwtService jwtService;

    private final LoginService loginService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    // JWT 인증 정보를 현재 쓰레드의 SecurityContext에 저장(가입/로그인/재발급 Request 제외)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // "/api2/login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 현재 필터 진행 막기
        }

        // 사용자 요청 헤더에서 RefreshToken 추출
        String accessToken = jwtService.extractAccessToken(request).filter(jwtService::isTokenValid)
                .orElse(null);
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid).orElse(null);
        String username = jwtService.extractUsername(accessToken).orElse(null);

        // 로그아웃된 계정인지 검증
        if (accessToken != null && redisTemplate.hasKey(accessToken)) {
            return;
        }

        // RefreshToken이 요청 헤더에 존재했다면, 사용자가 AccessToken이 만료되어서
        // RefreshToken까지 보낸 것이므로 리프레시 토큰이 DB의 리프레시 토큰과 일치하는지 판단 후,
        // 일치한다면 AccessToken을 재발급
        if (refreshToken != null) {
            checkUsernameAndReIssueToken(response, refreshToken, username);
            return; // RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 return으로 필터 진행 막기
        }

        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    // [username으로 유저 정보 찾기 & 액세스 토큰/리프레시 토큰 재발급]
    public void checkUsernameAndReIssueToken(HttpServletResponse response, String refreshToken,
            String username) {
        // 헤더에서 추출한 username로 Redis에서 유저를 찾고, 해당 유저가 있다면
        if (redisTemplate.hasKey(username)) {
            // reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트
            String reIssuedAccessToken = jwtService.createAccessToken(username);
            String reIssuedRefreshToken = reIssueRefreshToken(username);

            jwtService.sendAccessAndRefreshToken(response, reIssuedAccessToken,
                    reIssuedRefreshToken);
        }

        // DB에 업데이트 시
        // loginService.findByRefreshToken(refreshToken)
        // .ifPresent(user -> {
        // // reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트
        // String username = user.getUsername();
        // String reIssuedAccessToken = jwtService.createAccessToken(username)
        // String reIssuedRefreshToken = reIssueRefreshToken(user);

        // // JwtService.createAccessToken()으로 AccessToken 생성,
        // // 그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
        // jwtService.sendAccessAndRefreshToken(response, reIssuedAccessToken,
        // reIssuedRefreshToken);
        // });
    }

    // [리프레시 토큰 재발급 & Redis에 리프레시 토큰 업데이트]
    private String reIssueRefreshToken(String username) {
        // jwtService.createRefreshToken()으로 리프레시 토큰 재발급 후
        String reIssuedRefreshToken = jwtService.createRefreshToken(username);

        // Redis에 재발급한 리프레시 토큰 업데이트
        jwtService.setRefreshTokenRedis(username, reIssuedRefreshToken);
        // DB에 업데이트 시
        // loginService.updateRefreshToken(user);

        return reIssuedRefreshToken;
    }

    /**
     * [액세스 토큰 체크 & 인증 처리 메소드] 그 유저 객체를 saveAuthentication()으로 인증 처리하여 인증 허가 처리된 객체를
     * SecurityContextHolder에 담기 그 후 다음 인증 필터로 진행
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");

        // request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid)
                // 유효한 토큰이면, 액세스 토큰에서 extractUsername으로 username을 추출한 후
                .ifPresent(accessToken2 -> jwtService.extractUsername(accessToken2)
                        // findByUsername()로 해당 유저 객체 반환
                        .ifPresent(username -> loginService.findByUsername(username)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    /**
     * [인증 허가 메소드] 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     *
     * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
     * UsernamePasswordAuthenticationToken의 파라미터 1. 위에서 만든 UserDetailsUser 객체 (유저 정보) 2.
     * credential(보통 비밀번호로, 인증 시에는 보통 null로 제거) 3. Collection < ? extends GrantedAuthority>로,
     * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에, new
     * NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
     *
     * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후, setAuthentication()을 이용하여 위에서 만든
     * Authentication 객체에 대한 인증 허가 처리
     */
    public void saveAuthentication(User myUser) {
        String password = myUser.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = "aa";
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getUsername()).password(password).roles(myUser.getRole()).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser,
                null, authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
