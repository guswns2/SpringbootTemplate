package com.jhj.template.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class User {

    private String username, password, role, refreshToken;

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

}
