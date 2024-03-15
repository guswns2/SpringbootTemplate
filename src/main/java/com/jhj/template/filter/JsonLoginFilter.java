package com.jhj.template.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

// 로그인 필터   
// 스프링 시큐리티의 폼 기반의 UsernamePasswordAuthenticationFilter를 참고하여 만든 커스텀 필터
// 거의 구조가 같고, Type이 Json인 Login만 처리하도록 설정한 부분만 다르다. (커스텀 API용 필터 구현)
public class JsonLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final String DEFAULT_LOGIN_REQUEST_URL = "/api2/login"; // "/login"으로 오는 요청을 처리
	private static final String HTTP_METHOD = "POST"; // 로그인 HTTP 메소드는 POST
	private static final String CONTENT_TYPE = "application/json"; // JSON 타입의 데이터로 오는 로그인 요청만 처리
	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";
	private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
			DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); // "api2/login" + POST로 온 요청에 매칭된다.

	private final ObjectMapper objectMapper;

	public JsonLoginFilter(ObjectMapper objectMapper) {
		super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); // 위에서 설정한 "api2/login" + POST로 온 요청을 처리하기 위해 설정
		this.objectMapper = objectMapper;
	}

	// 인증 처리 메소드
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {
		// 요청의 ContentType이 null이거나 우리가 설정한 application/json이 아니라면, 예외를 발생시켜 JSON으로 요청
		if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
			throw new AuthenticationServiceException(
					"Authentication Content-Type not supported: " + request.getContentType());
		}

		// StreamUtils를 통해 request에서 messageBody(JSON) 반환
		String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

		// 꺼낸 messageBody를 objectMapper.readValue()로 Map으로 변환 (Key : JSON의 키 ->
		// username, password)
		Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

		// Map의 Key로 아이디, 패스워드 추출 후
		String username = usernamePasswordMap.get(USERNAME_KEY);
		String password = usernamePasswordMap.get(PASSWORD_KEY);

		// principal, credentials에 대입
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

		// AbstractAuthenticationProcessingFilter(부모)의 getAuthenticationManager()로
		// AuthenticationManager 객체를 반환 받은 후
		// authenticate()의 파라미터로 UsernamePasswordAuthenticationToken 객체를 넣고 인증 처리
		// (여기서 AuthenticationManager 객체는 ProviderManager -> SecurityConfig에서 설정)
		return this.getAuthenticationManager().authenticate(authRequest);
	}
}