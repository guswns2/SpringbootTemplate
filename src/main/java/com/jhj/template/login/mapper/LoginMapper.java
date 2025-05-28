package com.jhj.template.login.mapper;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import com.jhj.template.login.model.User;

@Mapper
public interface LoginMapper {

    Optional<User> findByUsername(String username);

    Optional<User> findByRefreshToken(String refreshToken);

    void updateRefreshToken(User user);

    void signup(User user);

}
