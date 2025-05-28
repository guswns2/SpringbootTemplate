package com.jhj.template;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.util.StringUtils;

public class JasyptApplication {

    static String enc = "X8AvHMcpXE5uYOcgTKyDvXgrrb4my/aKQBOkrdvQIq75ZIiF+RgGIg==";
    // static String enc = "";
    static String plain = "평문입니다";
    // static String plain= "";

    static String password = "b7878370-f5a6-4f9c-9aca-741253f71455";
    static String algorithm = "PBEWithMD5AndTripleDES";

    public static void main(String[] args) {

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password); // 암호화할 때 사용하는 키
        config.setAlgorithm(algorithm); // 암호화 알고리즘
        config.setKeyObtentionIterations("1000"); // 반복할 해싱 회수
        config.setPoolSize("1"); // 인스턴스 pool
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64"); // 인코딩 방식
        encryptor.setConfig(config);

        String plainText = "암호화문"; // 암호화 할 내용
        if (StringUtils.hasText(plain)) {
            plainText = plain;
        }
        String encryptedText = encryptor.encrypt(plainText); // 암호화

        if (StringUtils.hasText(enc)) {
            encryptedText = enc;
        }
        String decryptedText = encryptor.decrypt(encryptedText); // 복호화

        System.out.println("Enc : " + encryptedText);
        System.out.println("Dec : " + decryptedText);

    }
}
