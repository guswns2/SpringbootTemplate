package com.jhj.template.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jhj.template.common.Interceptor;

import org.springframework.lang.NonNull;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired
  @NonNull
  Interceptor interceptor;

  private final long MAX_AGE_SECS = 3600;

  // Interceptor 등록
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 적용할 url
    registry.addInterceptor(interceptor).addPathPatterns("/api2/**");

    // 제외할 url
    // registry.addInterceptor(interceptor)
    // .excludePathPatterns("/test/**", "/", "/index.html", "/app.css", "/app.js",
    // "/favicon.ico",
    // "/angular/**", "/assets/**", "/css/**", "/error/**", "/images/**",
    // "/libs/**", "/MARKER/**",
    // "/views/**", "/common/**");

    // 기본 인터셉터 설정을 유지
    // WebMvcConfigurer 인터페이스는 여러 메서드를 가지고 있고, 필요한 메서드만 오버라이드하여 사용
    // 특정 메서드를 오버라이드한 경우에는 나머지 메서드들의 기본 동작을 유지하기 위해 super 키워드를 사용
    WebMvcConfigurer.super.addInterceptors(registry);
  }

  // CORS 설정
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:8080")
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(MAX_AGE_SECS);

    WebMvcConfigurer.super.addCorsMappings(registry);
  }

}