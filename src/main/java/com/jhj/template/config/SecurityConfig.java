package com.jhj.template.config;

import com.jhj.template.filter.JsonLoginFilter;
import com.jhj.template.filter.JwtAuthenticationFilter;
import com.jhj.template.handler.LoginFailureHandler;
import com.jhj.template.handler.LoginSuccessHandler;
import com.jhj.template.mapper.LoginMapper;
import com.jhj.template.service.JwtService;
import com.jhj.template.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

// 인증은 JsonLoginFilter에서 authenticate()로 인증된
// 사용자로 처리
// JwtAuthenticationFilter는 AccessToken, RefreshToken 재발급
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final LoginService loginService;
	private final JwtService jwtService;
	private final ObjectMapper objectMapper;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// Security 인증 무시할 경로
		return web -> web.ignoring()
				.requestMatchers("/swagger-ui/**", "/v3/**");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// .httpBasic(basic -> basic.disable()) // httpBasic 사용 X
				.csrf(csrf -> csrf.disable()) // csrf 보안 사용 X
				.cors(cors -> cors.disable()) // cors 보안 사용 X
				// .headers(headers -> headers.frameOptions().disable()) // http 응답 헤더 구성
				// 세션 사용하지 않으므로 STATELESS로 설정
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// == URL별 권한 관리 옵션 ==//
				.authorizeHttpRequests(authorize -> authorize
						// .dispatcherTypeMatchers(DispatcherType.FORWARD,
						// DispatcherType.ERROR).permitAll()
						.requestMatchers("/api2/getAuthorization").authenticated() // 인증되었을 때만 접근 가능
						.anyRequest().permitAll()) // 인증 없이 접근 가능
				// .and()
				.formLogin(login -> login.disable()) // FormLogin 사용 X
				// .formLogin(login -> login // form 방식 로그인 사용
				// .loginPage("http://localhost:8080") // 사용자가 정의한 로그인 페이지
				// .loginProcessingUrl("/api2/login") // 로그인 인증 요청을 보낼 경로
				// .defaultSuccessUrl("http://localhost:8080/swagger-ui/index.html") // 로그인 인증
				// 성공 시 이동할 경로
				// // .failureUrl("http://localhost:8080")
				// // .successHandler() // 로그인 인증 성공 후 별도 처리가 필요한 경우 커스텀 핸들러 생성하여 등록
				// // failureHandler() // 로그인 실패 후 별도 처리 핸들러
				// .permitAll()) // 사용자 정의 로그인 페이지 접근 모두 가능
				.logout((logout) -> logout
						.disable()
				// .logoutUrl("/api2/logout")
				// .addLogoutHandler(logoutHandler)
				// .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
				// .logoutSuccessUrl("/swagger-ui/index.html")
				)
				// 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
				// 순서 : LogoutFilter -> JwtAuthenticationFilter ->
				// JsonLoginFilter
				.addFilterAfter(jsonLoginFilter(),
						LogoutFilter.class)
				.addFilterBefore(jwtAuthenticationFilter(),
						JsonLoginFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	// AuthenticationManager 설정 후 등록
	// PasswordEncoder를 사용하는 AuthenticationProvider 지정
	// FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
	// UserDetailsService는 커스텀 LoginService로 등록
	// FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return
	// ProviderManager)
	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(loginService);
		return new ProviderManager(provider);
	}

	// 로그인 성공 시 호출되는 LoginSuccessHandler 빈 등록
	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler(jwtService, loginService);
	}

	// 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
	@Bean
	public LoginFailureHandler loginFailureHandler() {
		return new LoginFailureHandler();
	}

	// jsonLoginFilter 빈 등록
	// 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
	// setAuthenticationManager(authenticationManager())로 위에서 등록한
	// AuthenticationManager(ProviderManager) 설정
	// 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
	@Bean
	public JsonLoginFilter jsonLoginFilter() {
		JsonLoginFilter jsonLoginFilter = new JsonLoginFilter(
				objectMapper);
		jsonLoginFilter.setAuthenticationManager(authenticationManager());
		jsonLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
		jsonLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
		return jsonLoginFilter;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService,
				loginService);
		return jwtAuthenticationFilter;
	}
}
