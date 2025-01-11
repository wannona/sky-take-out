package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.springframework.stereotype.Component;

public interface UserService {

    /**
     * 微信登录
     * @param loginDTO
     * @return
     */
    User wxLogin(UserLoginDTO loginDTO);
}
