package com.jhj.template.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jhj.template.common.Properties;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    @Autowired
    Properties properties;

    // API 그룹 설정
    // Swagger UI 헤더의 Select a definition에 표시
    @Bean
    public GroupedOpenApi openApi1() {
        // Controller API 경로와 맞춰주기
        String[] paths = {"/api1/**"};

        return GroupedOpenApi.builder().group("Rest API").pathsToMatch(paths).build();
    }

    @Bean
    public GroupedOpenApi openApi2() {
        String[] paths = {"/api2/**"};

        return GroupedOpenApi.builder().group("Login API").pathsToMatch(paths).build();
    }

    @Bean
    public GroupedOpenApi openApi3() {
        String[] paths = {"/api3/**"};

        return GroupedOpenApi.builder().group("Paging API").pathsToMatch(paths).build();
    }

    @Bean
    public GroupedOpenApi openApi4() {
        String[] paths = {"/api4/**"};

        return GroupedOpenApi.builder().group("Redis API").pathsToMatch(paths).build();
    }

    @Bean
    public GroupedOpenApi openApi5() {
        String[] paths = {"/api5/**"};

        return GroupedOpenApi.builder().group("Kafka API").pathsToMatch(paths).build();
    }

    @Bean
    public GroupedOpenApi openApi6() {
        String[] paths = {"/api6/**"};

        return GroupedOpenApi.builder().group("Mqtt API").pathsToMatch(paths).build();
    }

    @Bean
    public GroupedOpenApi openApi7() {
        String[] paths = {"/api7/**"};

        return GroupedOpenApi.builder().group("Cache API").pathsToMatch(paths).build();
    }

    @Bean
    public GroupedOpenApi openApi8() {
        String[] paths = {"/api8/**"};

        return GroupedOpenApi.builder().group("Session API").pathsToMatch(paths).build();
    }

    // Swagger 정보
    private Info apiInfo() {
        return new Info().title("Template API").description("Template Swagger UI").version("2.3.0");
    }

    // 권한 인증
    // Swagger UI에서 Authorize 버튼 누르면 나오는 SecurityScheme 명
    String jwtSchemeName = "Authorization";
    // SecurityScheme 보안 체계
    SecurityScheme baererAuth =
            new SecurityScheme().name(jwtSchemeName).type(SecurityScheme.Type.HTTP) // HTTP 방식
                    .scheme("bearer").bearerFormat("JWT");
    // API 요청헤더에 인증정보 포함
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
    // SecuritySchemes 등록
    Components components = new Components().addSecuritySchemes(jwtSchemeName, baererAuth); // 토큰
                                                                                            // 형식을
                                                                                            // 지정하는
                                                                                            // 임의의
                                                                                            // 문자(Optional)

    // Swagger 생성
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().components(components).info(apiInfo());
    }

}
