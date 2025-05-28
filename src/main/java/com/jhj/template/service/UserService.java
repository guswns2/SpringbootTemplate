package com.jhj.template.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhj.template.dto.User;
import com.jhj.template.mapper.LoginMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    public void signup(User user) {

        user.passwordEncode(passwordEncoder);
        loginMapper.signup(user);

    }
}
