package com.jhj.template.login.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jhj.template.login.mapper.LoginMapper;
import com.jhj.template.login.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginService implements UserDetailsService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Autowired
    LoginMapper loginMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = loginMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername()).password(user.getPassword()).roles(user.getRole())
                .build();
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> user = loginMapper.findByUsername(username);

        return user;
    };

    public Optional<User> findByRefreshToken(String refreshToken) {
        Optional<User> user = loginMapper.findByRefreshToken(refreshToken);

        return user;
    };

    public void updateRefreshToken(User user) {

        loginMapper.updateRefreshToken(user);

    };

}
