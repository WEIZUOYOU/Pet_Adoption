package com.pet.adoption.service;

import com.pet.adoption.common.Result;
import com.pet.adoption.dto.LoginRequest;
import com.pet.adoption.dto.RegisterRequest;
import javax.servlet.http.HttpSession;

public interface UserService {
    Result register(RegisterRequest request);
    Result login(LoginRequest request, HttpSession session);
    Result logout(HttpSession session);
    Result listUsers(Integer page, Integer size);
    Result updateUserStatus(Integer userId, Integer status);
}