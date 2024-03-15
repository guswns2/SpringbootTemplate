package com.jhj.template.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {
  // application.properties 암/복호화
  // jasypt 라이브러리 설정
  @Value("${jasypt.encryptor.password}")
  private String encryptKey;

  @Bean(name = "jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();

    config.setPassword(encryptKey); // 암호화할 때 사용하는 키
    // config.setAlgorithm("PBEWithMD5AndTripleDES"); // 암호화 알고리즘
    config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
    config.setKeyObtentionIterations("1000"); // 반복할 해싱 회수
    config.setPoolSize("1"); // 인스턴스 pool
    // config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
    config.setStringOutputType("base64"); // 인코딩 방식

    encryptor.setConfig(config);
    return encryptor;
  }
}
