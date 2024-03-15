# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

- [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
- [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.1/maven-plugin/reference/html/)
- [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.1/maven-plugin/reference/html/#build-image)

# 프로젝트 tab size 설정

EditorConfig 플러그인 설치 (vsCode, eclipse, intelliJ 등 가능)
.editorconfig에서 설정
(편집기 종류에 따라 코드 포맷이 변하게 하지 않기 위해 사용)

# Spring-Boot 버전

Initializer에서 Spring-boot 3.2.2 버전, 자바 17 버전, Maven 프로젝트 선택
Spring-boot 버전중 SNAPSHOT은 개발중인 버전이므로 불안정
Spring-boot 3 버전부터는 자바 17 버전 이상만 지원

# Java 버전

Spring-boot Maven 빌드는 pom.xml에서 Java 버전이 설정되어있으면 그 버전으로 빌드
단 PC에 그 버전이 설치되어 있어야 함
pom.xml에 설정이 안 되어있으면 PC 시스템 변수에 등록된 Java 버전을 사용하여 빌드

java -version 명령어로 나오는 결과는 시스템 변수 Path에 가장 위에 등록된 버전 표시
%JAVA_HOME%을 변경하면서 java 버전을 이용하고 싶으면 %JAVA_HOME%\bin을 가장 위에 올리면 됨

# 개발 환경 분리

application.properties => application-dev.properties, application-prod.properties
Properties.java 생성
@Value로 properties 값 읽어옴

launch.json, 또는 명령어로 어느 파일을 읽어올지 결정
"--spring.profiles.active=dev"

# Swagger

spring-fox, spring-doc 두 개의 라이브러리
fox는 이제 업데이트가 되지 않아 잘 사용하지 않음
spring 3 버전부터는 spring-doc 필수
springdoc-openapi-starter-webmvc-ui 버전 2 이상 의존성 추가

SwaggerConfig 생성

swagger 2에서 3으로 넘어오면서 바뀌는 Annotation 명
https://springdoc.org/#source-code-of-the-demo-applications

Annotation 설명
https://blog.jiniworld.me/156#a02-1

swagger 주소
localhost:8081/swagger-ui/index.html

# Swagger prod 환경에서 접근제한

application-prod.properties
springdoc.swagger-ui.enabled=false 설정
2.1.2 버전부터 적용됨
https://happy-jjang-a.tistory.com/215

# Swagger Authorization

SwaggerConfig에서 SecurityScheme 설정
Controller에서 @SecurityRequirement 설정. API 단위에도 설정 가능
SecurityScheme 명과 @SecurityRequirement에 설정한 name 값 일치해야 함

https://happy-jjang-a.tistory.com/165
https://velog.io/@hwan2da/Spring-Swagger-UI-SpringDoc-OpenAPI-01

<!-- # DB MySQL 연동

MySQL 설치 및 로컬서버 실행
MySQL, Mybatis dependency 추가
MySQL application.properties 설정 추가

Dbeaver에서 서버 연결할 때 Public Key Retrueval is not allowed 오류
MySQL 8 버전 이상 설치 시 발생
Edit Connection -> Driver properties
allowPublicKeyRetrueval = true, useSSL = false

프로젝트 구성 방법
둘 다 동적쿼리는 가능

1. Controller, Service, Mapper, XML => @Mapper 가독성 좋고, resultType 자유로움
2. Controller, Service, ServiceImpl, Mapper => mapper.xml 가독성 안 좋고, resultType DTO만 가능

https://velog.io/@bi-sz/MySQL-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0 -->

# Restful API HTTP 메서드

GET: 리소스 조회에 사용
HEAD: 응답에 Body가 없고 코드와 Header만 받을 수 있음
POST: 새로운 리소스 생성에 사용
PUT: 특정 리소스 전체 수정에 사용
PATCH: 특정 리소스 일부 수정에 사용
DELETE: 특정 리소스 삭제에 사용
CONNECT: 동적으로 터널 모드를 교환, 프락시 기능을 요청 시 사용
TRACE: 원격지 서버에 루프백 메시지 호출하기 위해 테스트용으로 사용
OPTIONS: 웹서버에서 지원되는 메소드의 종류를 Header에서 확인 가능

https://yuricoding.tistory.com/79

# API @RequestBody Json 속성값 null 오류

@RequestBody import를 swagger가 아닌 spring에서 해줘야 함..
출처: https://sc-science.tistory.com/85

# properties DB 비밀번호 암호화

pom.xml
jasypt 라이브러리 의존성, 플러그인 추가

application-properties
spring.datasource.password=ENC(암호화된 비밀번호)
jasypt.encryptor.password = 암/복호화에 사용할 마스터 비밀번호
jasypt.encryptor.property.prefix=ENC(
jasypt.encryptor.property.suffix=)

JasyptConfig 생성 및 설정

출처: https://kooremo.tistory.com/entry/spring-boot-applicationproperties-DB%EC%A0%91%EC%86%8D-%EC%A0%95%EB%B3%B4-%EC%95%94%ED%98%B8%ED%99%94

암/복호화 테스트
JasyptApplication.java 실행 또는 MvnJasypt.bat에 있는 명령어 실행

# DB MySQL 대신 SQLite 적용

pom.xml
sqlite-jdbc
hibernate-community-dialects
의존성 주입

application.properties
앱 실행하면 url 경로로 .db 파일 생성
데이터베이스 관리도구(DBeaver)에서 .db 파일 경로로 연결하면 생성된 테이블 볼 수 있음

# Jackson 라이브러리

@JsonProperty: 필드 또는 메서드에 사용. JSON 속성의 이름을 지정. Java 클래스의 필드명과 JSON 속성명이 다를 때 사용
@JsonInclude: 객체 직렬화(객체 -> JSON) 시에 특정 조건에 따라 포함 또는 제외될 속성을 지정

# WebMvcConfig

WebMvcConfig.java 생성

# Interceptor

Interceptor.java 생성
WebMvcConfig에 addInterceptors 메서드 오버라이딩
출처: https://livenow14.tistory.com/61

# CORS

CORS(Cross-Origin Resource Sharing): 서로 다른 출처의 자원을 공유할 수 있도록 설정하는 권한
Vue.js에서 Axios 등을 사용하여 Spring Boot API를 호출하는 경우, 이는 주로 브라우저에서 실행되기 때문에 CORS 정책이 적용
웹 브라우저가 아닌 다른 환경에서 직접 URL로 API를 호출하는 경우에는 브라우저의 보안 정책이 적용 안 됨

# Scheduler

main thread(WasApplication)에 @EnableScheduling 추가
SchedulerConfig 생성 후 @Component 등록
주기적으로 실행할 메서드에 @Shcheduled 등록
출처: https://dev-coco.tistory.com/176
https://velog.io/@penrose_15/Scheduler%EC%99%80-cron-%EC%82%AC%EC%9A%A9%EB%B2%95 (@Async)

# WebSocket

TCP 기반 양방향 통신 프로토콜
주로 실시간 게임, 채팅, 주식 등에서 사용

pom.xml
의존성 주입

WebSocketConfig, WebSocketHandler 생성

Chrome Simple Websocket Client 확장 프로그램으로 테스트

Security 사용 시 SecurityConfig에 웹소켓 연결 주소 허용해줘야 함

출처: https://yjkim-dev.tistory.com/65

# STOMP

WebSocket과 함께 사용되어 WebSocket 위에서 동작
메시지 기반 통신으로 메시지 큐를 이용하여 전송 및 구독

웹에서 GET url/Endpoint/iframe.html 404 에러 나면
웹에서 연결할 때 http://서버 IP:서버 PORT/Endpoint 전체 명시

StompConfig, StompController, Socket 생성
출처: https://velog.io/@skyepodium/vue-spring-boot-stomp-%EC%9B%B9%EC%86%8C%EC%BC%93

# Spring Security

Spring Security는 주로 보안 및 인증과 관련된 기능 구현
interceptor는 요청과 응답을 가로채거나 조작하여 비즈니스 로직과 관련된 부가적인 작업을 수행

로그인 기능 구현
pom.xml에 의존성 추가
SecurityConfig 생성
출처: https://dnl1029.tistory.com/35

SecurityConfig 인증 관련 설정
https://velog.io/@wooyong99/Spring-Security
https://docs.spring.io/spring-security/reference/servlet/configuration/java.html

SecurityConfig FilterChain. Form 로그인
https://velog.io/@djc06048/%EC%8A%A4%ED%94%84%EB%A7%81%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-webSecurityCustomizer-%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC

SecurityConfig JWT + JSON 로그인
https://ksh-coding.tistory.com/57

form 로그인 방식 + 세션 인증 (아직 세션 인증 기능은 미적용)
json 로그인 방식 + jwt 인증

로그인 성공 시 받은 액세스 토큰을 api 요청할 때 헤더에 Authorization : Bearer (토큰) 넣어줘야 함

# logback 설정

터미널 대신 디버그 콘솔에서 로그를 보고싶다면
launch.json 에 "console": "internalConsole" 추가

https://samori.tistory.com/69

# 테이블 데이터 페이징

PageInfo.java, PagingSet.java 추가
TotalDataCnt.java 생성 후 DTO(Test.java)에 상속

PagingController, PagingService, PagingMapper, Paging.xml 생성 및 테스트

# static

https://coding-factory.tistory.com/524

# Utils

StringBuilderPlus
https://myeongju00.tistory.com/61

JsonUtil
pom.xml에 googlecode.json-simple 추가

# Redis

WSL 설치
Redis 설치
https://redis.com/blog/install-redis-windows-11/

Ubuntu redis server 관리 명령어
sudo service redis-server start
sudo service redis-server stop
sudo service redis-server status

redis CRUD 명령어
https://freeblogger.tistory.com/10#google_vignette

RedisConfig, RedisController 생성

redis는 데이터 수정은 불가능. 덮어쓰기만 가능

Lettuce, Jedis
Lettuce와 Jedis는 둘 다 Java 언어로 작성된 Redis 클라이언트 라이브러리. 이 두 라이브러리는 Redis 서버와 통신하여 데이터를 저장, 검색 및 조작하는 데 사용

Lettuce
Lettuce는 비동기 및 논블로킹 IO를 지원하는 Redis 클라이언트. 비동기 및 논블로킹 IO를 지원하기 때문에 높은 성능과 확장성을 제공.
Lettuce는 Netty를 기반으로하며, 이는 다수의 연결을 효율적으로 관리. Lettuce는 스레드 안전하며, 여러 스레드에서 공유될 수 있습니다.
또한 Redis Sentinel 및 Redis Cluster와 같은 Redis의 고가용성 및 분산 기능을 지원.

Jedis
Jedis는 단순하고 직관적인 사용을 지향하는 Redis 클라이언트. 기본적으로는 동기식 IO를 사용. Jedis를 단일 쓰레드 환경에서 가장 잘 작동.
Jedis는 Apache 2.0 라이센스를 따르는 오픈 소스 프로젝트이며, 단순한 구조와 사용하기 쉬운 API를 제공.
그러나 여러 스레드에서 Jedis 인스턴스를 공유할 때 발생할 수 있는 문제

# Security + JWT + Redis 로그아웃 구현

1. 로그인 시 redis에 refreshToken 저장
   LoginService updateRefreshToken - redis에 refreshToken 저장 로직 추가

2. updateRefreshToken 메서드 사용하는 곳에 username 파라미터 추가
   LoginSuccessHandler onAuthenticationSuccess
   jwtAuthenticationFilter reIssueRefreshToken

3. 로그아웃 시 accessToken 블랙리스트(redis)에 추가
   LoginController logout - redis에서 username(key): refreshToken(value) 삭제, accessToken(key): 로그아웃 상태(value) 저장 로직 추가
   JwtService getExpiration 추가

4. api 요청을 받는 인증 과정에서 로그아웃 검증
   JwtAuthenticationFilter doFilterInternal - accessToken이 블랙리스트(redis)에 있는지 검증 로직 추가

<!-- 현재 코드에서는 Redis에 RefreshToken 토큰을 저장하여 활용하는데,
		실제 프로젝트에서는 DB, Redis 중 하나를 선택해서 활용하면 될 듯
		그에 맞춰서 토큰 생성, 재발급, 인증 등 수정 필요 -->

https://velog.io/@joonghyun/SpringBoot-Jwt%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%A1%9C%EA%B7%B8%EC%95%84%EC%9B%83
https://geunzrial.tistory.com/179
https://dev-chw.tistory.com/30
https://velog.io/@debug/Spring-security-JWT-Redis-6-logout

# Kafka 설치 및 테스트 + Spring Boot 연동

- Kafka 설치
  Kafka 설치 전에 자바, 스칼라 설치되어있어야 함
  자바는 8버전 이상, 스칼라는 Kafka에서 받은 버전과 같은 버전
  2.13.13 버전 설치했음

Kafka 홈페이지에서 3.7 scala 2.13 버전 다운로드 후
임의의 파일에서 압축 해제(경로 짧은 곳에 설치. 윈도우는 경로가 길면 명령어 실행 오류 남)

- Kafka 실행
  각각 다른 shell을 열어서 (cmd, powershell 등) kafka_2.13-3.7.0 파일 경로로 이동하여
  zookeeper 실행(default 2181 포트에서 실행) -> kafka 4.0부터는 zookeeper 의존성을 지운 채로 출시될 예정이기 때문에 실행할 필요 없을 듯
  bin\windows\zookeeper-server-start.bat config\zookeeper.properties
  kafka 실행
  bin\windows\kafka-server-start.bat config\server.properties
  (참고한 블로그에서는 파일이 .sh 확장자로 되어있지만 실제 다운로드 받은 파일엔 .bat 확장자 파일로 들어있었음)

- Kafka 테스트
  topic 생성
  bin\windows\kafka-topics.bat --create --topic my-topic --bootstrap-server localhost:9092 --partitions 1
  topic 목록 조회
  bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --list
  (kafka가 2181 포트에서 실행되었다고 해서 localhost:2181로 실행하면 오류남. 새로운 버전에서는 9092로 바인딩해줘야 함)

마찬가지로 shell을 따로 열어서
발행
bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic my-topic
구독
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 -topic my-topic --from-beginning

- Spring Boot와 연동
  pom.xml
  의존성 추가

application-properties
producer, consumer property 추가

ProducerConfig, ConsumerConfig, KafkaController 추가하여 테스트

테스트 과정에서 SpringBoot 실행 시 Kafka 관련 디버그 로그가 너무 많이 찍혀서, logback.xml에서 Kafka 관련 로그 제외

https://stackoverflow.com/questions/71866910/error-in-zookeeper-unreasonable-length-308375649-when-creating-topic-in-kafk

# MQTT

eclipse mosquitto(브로커) 설치
mosquitto 실행은 작업관리자 - 서비스 에서 직접 실행해줘야 함
https://pros2.tistory.com/137

pom.xml
spring-integration-mqtt
org.eclipse.paho.client.mqttv3
의존성 추가

MqttConfig, MqttController 생성 후 테스트

!! mqttClient.connect()에서 connection lost - 연결 유실 오류가 날 경우 !!
MqttConfig에서 mqttClient(), messageDrivenChannelAdapter()에 작성된 clientId가 같은 id로 작성되어있는지 확인
각각 다른 id로 설정해줘야함

https://titlejjk.tistory.com/323?category=1111611
https://velog.io/@soohyuneeee/JAVA-SpringBoot-MQTT-Broker-%EC%97%B0%EB%8F%99%ED%95%98%EA%B8%B0

# Cache

Springboot 내장 캐시 저장소 또는 Redis에서 주로 관리

- Springboot 내장 캐시 저장소에서 관리하는 방법
  pom.xml
  의존성 추가

CacheConfig, CacheController, CacheService, CacheMapper, Cache.xml 생성 및 테스트

https://conanmoon.medium.com/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D-%EC%9D%BC%EA%B8%B0-spring%EC%97%90%EC%84%9C%EC%9D%98-%EC%BA%90%EC%8B%B1-caching-9221631b4051

- Redis에서 관리하는 방법
  RedisConfig
  contentCacheManager 추가

CacheService
=> Redis에서 캐시 관리할 때는 Service를 Mapper에서 상속받지 않는 게 나아보임.
MyBatis update 쿼리 resultType이 int인데 Service단에서도 Override를 하면 int를 return 해야하기 때문에 Redis에도 value int 값이 저장됨.

https://www.wool-dev.com/backend-engineering/spring/springboot-redis-cache

!! SpringBoot 내장 캐시 CacheConfig의 CacheManager와
RedisConfig의 CacheConfig의 Bean이 중복 등록되어 앱이 실행되지 않을 때는
사용할 CacheManager에 @Primary 애너테이션 추가하면 됨 !!

# Session

SessionController 생성 및 테스트
Swagger보다는 Postman을 통해 테스트 해보면 세션이 생성되고 나서
응답의 쿠키에도 'SESSION'을 키로 세션 ID가 들어가있는 것을 확인 가능

발급받은 세션은 Redis 등 저장소에서 관리
클라이언트는 로그인 후 발급받은 세션 id로 서버에 요청 시 인증을 받으며 로그인 상태 유지

Redis에서 관리하는 방법
pom.xml
spring-session-data-redis 의존성 추가

RedisConfig
@EnableRedisHttpSession
추가하면 세션 생성 시 자동으로 Redis에 저장

https://jong-bae.tistory.com/30

# Docker

Docker를 사용하면 외부 서버로 실행시켰던 프로그램들(Redis, Kafka 등등)을
한 곳에서 설치하고 실행할 수 있음

WSL2 설치
https://www.lainyzine.com/ko/article/a-complete-guide-to-how-to-install-docker-desktop-on-windows-10/

Docker Desktop 설치
설치하면 docker-compose도 같이 설치됨

Docker Desktop
Redis, Kafka, Zookeeper, Eclipse Mosquitto 이미지 설치

아무 파일 생성 후 그 안에 docker-compose.yml 파일 생성(프로젝트 파일 내에도 가능)
yml 파일에서 프로그램별 properties 정리

명령 프롬프트에서 yml 파일이 존재하는 경로로 들어가
docker-compose up -d 실행하면 모든 프로그램 실행됨

https://engschool.tistory.com/212

# Build

생성
pom.xml에 생성할 war 파일명 설정
<finalName>template.was-1.0</finalName>

터미널에서 mvn clean package -Dspring.profiles.active=dev 실행 (mvnpackage.bat)
(빌드할때는 프로젝트의 자바 버전과 PC의 java -version을 맞춰줘야 함. 로컬에서 실행할 때는 안 맞아도 상관없음)

실행
cd target
java -jar template.was-1.0.war --spring.config.location=file:../src/main/resources/application-dev.properties --spring.profiles.active=dev

# 배포 시 정적 파일(UI 파일 등) 저장 경로 설정

application-dev.properties
server.tomcat.basedir=./webapp

Properties 필드 추가

SpringbootTemplateApplication
File file = new File(baseDir);
tomcat.setDocumentRoot(file);

setDocumentRoot()
서버의 문서 루트(Document Root)를 설정
Apache Tomcat은 Java 웹 애플리케이션 서버로,
정적인 웹 자원(HTML 파일, 이미지, CSS 파일 등)을 제공하기 위해 특정 디렉토리를 설정하는데, 이 디렉토리가 바로 문서 루트
클라이언트가 서버에 요청하는 URL 경로에 해당하는 파일이나 디렉토리를 이 디렉토리에서 찾음.
스프링 부트와 같은 프레임워크에서는 보통 src/main/resources/static 또는 src/main/resources/public 디렉토리 등이 기본적으로 문서 루트로 설정

# 외장 Tomcat으로 프로젝트 실행

https://kitty-geno.tistory.com/191
