package com.pet.adoption.controller;

import com.pet.adoption.common.Result;
import com.pet.adoption.dto.LoginRequest;
import com.pet.adoption.dto.RegisterRequest;
import com.pet.adoption.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户注册", description = "新用户注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @Operation(summary = "用户登录", description = "用户登录接口，成功后会在session中保存用户信息")
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest request, HttpSession session) {
        return userService.login(request, session);
    }

    @Operation(summary = "用户登出", description = "用户登出接口，清除session中的用户信息")
    @GetMapping("/logout")
    public Result logout(HttpSession session) {
        return userService.logout(session);
    }
}