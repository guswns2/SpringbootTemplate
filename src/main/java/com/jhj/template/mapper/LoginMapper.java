package com.jhj.template.mapper;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.jhj.template.dto.User;

@Repository
public interface LoginMapper {

  Optional<User> findByUsername(String username);

  Optional<User> findByRefreshToken(String refreshToken);

  void updateRefreshToken(User user);

  void signup(User user);

}
