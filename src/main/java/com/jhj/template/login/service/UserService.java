package com.jhj.template.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.jhj.template.login.mapper.LoginMapper;
import com.jhj.template.login.model.User;

@Service
public class UserService {
    @Autowired
    LoginMapper loginMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void signup(User user) {

        user.passwordEncode(passwordEncoder);
        loginMapper.signup(user);

    }
}
