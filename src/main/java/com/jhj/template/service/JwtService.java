package com.jhj.template.service;

import com.jhj.template.mapper.LoginMapper;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;

	@Value("${jwt.access.header}")
	private String accessHeader;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;

	@Value("${jwt.refresh.header}")
	private String refreshHeader;

	// JWT의 Subject와 Claim으로 username 사용 -> 클레임의 name을 "username"으로 설정
	// JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	private static final String USERNAME_CLAIM = "username";
	private static final String BEARER = "Bearer ";

	private final LoginMapper loginMapper;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	// AccessToken 생성
	public String createAccessToken(String username) {
		Date now = new Date();
		return JWT.create() // JWT 토큰을 생성하는 빌더 반환
				.withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
				.withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정
				.withClaim(USERNAME_CLAIM, username) // .withClaim(클래임 이름, 클래임 값) 으로 추가 가능
				.sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-dev.properties에서 지정한 secretKey로 암호화
	}

	// AccessToken 만료시간 가져오기
	public Long getExpiration(String accessToken) {
		// accessToken 남은 유효시간
		Long expiration = JWT.decode(accessToken).getExpiresAt().getTime();
		// 현재 시간
		Long now = new Date().getTime();

		return expiration - now;
	}

	// RefreshToken 생성
	public String createRefreshToken(String username) {
		Date now = new Date();

		String refreshToken = JWT.create()
				.withSubject(REFRESH_TOKEN_SUBJECT)
				.withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
				.sign(Algorithm.HMAC512(secretKey));

		return refreshToken;
	}

	// AccessToken 헤더에 실어서 보내기
	public void sendAccessToken(HttpServletResponse response, String accessToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		response.setHeader(accessHeader, accessToken);
		log.info("재발급된 Access Token : {}", accessToken);
	}

	// AccessToken + RefreshToken 헤더에 실어서 보내기
	public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		setAccessTokenHeader(response, accessToken);
		setRefreshTokenHeader(response, refreshToken);
		log.info("Access Token, Refresh Token 헤더 설정 완료");
	}

	// 헤더에서 AccessToken 추출
	// 토큰 형식 : Bearer (토큰). "Bearer"를 삭제(""로 replace)
	public Optional<String> extractAccessToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(accessHeader))
				.filter(accessToken -> accessToken.startsWith(BEARER))
				.map(accessToken -> accessToken.replace(BEARER, ""));
	}

	// 헤더에서 RefreshToken 추출
	// 토큰 형식 : Bearer (토큰). "Bearer"를 삭제(""로 replace)
	public Optional<String> extractRefreshToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(refreshHeader))
				.filter(refreshToken -> refreshToken.startsWith(BEARER))
				.map(refreshToken -> refreshToken.replace(BEARER, ""));
	}

	// AccessToken에서 username 추출
	// 추출 전에 JWT.require()로 검증기 생성
	// verify로 AceessToken 검증 후
	// 유효하다면 getClaim()으로 username 추출
	// 유효하지 않다면 빈 Optional 객체 반환
	public Optional<String> extractUsername(String accessToken) {
		try {
			return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
					.build()
					.verify(accessToken)
					.getClaim(USERNAME_CLAIM)
					.asString());
		} catch (Exception e) {
			log.error("액세스 토큰이 유효하지 않습니다.");
			return Optional.empty();
		}
	}

	// AccessToken 헤더 설정
	public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
		response.setHeader(accessHeader, accessToken);
	}

	// RefreshToken 헤더 설정
	public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
		response.setHeader(refreshHeader, refreshToken);
	}

	// RefreshToken Redis 저장(업데이트)
	public void setRefreshTokenRedis(String username, String refreshToken) {
		// redis에 저장
		redisTemplate.opsForValue().set(
				username,
				refreshToken,
				refreshTokenExpirationPeriod,
				TimeUnit.MILLISECONDS);
	}

	// 토큰 유효성 검사
	public boolean isTokenValid(String token) {
		try {
			JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);

			return true;
		} catch (Exception e) {
			log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
			return false;
		}
	}
}
