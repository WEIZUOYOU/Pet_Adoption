package com.pet.adoption.service.impl;

import com.pet.adoption.common.Result;
import com.pet.adoption.common.SessionUtils;
import com.pet.adoption.dto.LoginRequest;
import com.pet.adoption.dto.RegisterRequest;
import com.pet.adoption.entity.User;
import com.pet.adoption.repository.UserRepository;
import com.pet.adoption.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Result register(RegisterRequest request) {
        if (userRepository.findByAccount(request.getAccount()) != null) {
            return Result.error("账号已被注册");
        }
        User user = new User();
        user.setAccount(request.getAccount());
        // 简单MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setRole(0);
        user.setStatus(0);
        userRepository.save(user);
        return Result.success("注册成功");
    }

    @Override
    public Result login(LoginRequest request, HttpSession session) {
        User user = userRepository.findByAccount(request.getAccount());
        if (user == null) {
            return Result.error("账号不存在");
        }
        String md5Password = DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!user.getPassword().equals(md5Password)) {
            return Result.error("密码错误");
        }
        if (user.getStatus() == 1) {
            return Result.error("账号已被禁用，请联系管理员");
        }
        // 隐藏密码后存入session
        user.setPassword(null);
        SessionUtils.setUser(session, user);
        return Result.success("登录成功", user);
    }

    @Override
    public Result logout(HttpSession session) {
        SessionUtils.removeUser(session);
        return Result.success("已退出登录");
    }
}