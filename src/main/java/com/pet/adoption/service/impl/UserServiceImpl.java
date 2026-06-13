package com.pet.adoption.service.impl;

import com.pet.adoption.common.Result;
import com.pet.adoption.common.SessionUtils;
import com.pet.adoption.dto.LoginRequest;
import com.pet.adoption.dto.RegisterRequest;
import com.pet.adoption.entity.User;
import com.pet.adoption.repository.UserRepository;
import com.pet.adoption.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Result register(RegisterRequest request) {
        // 验证账号
        if (request.getAccount() == null || request.getAccount().trim().isEmpty()) {
            return Result.error("账号不能为空");
        }
        
        // 验证密码
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return Result.error("密码长度不能少于6位");
        }
        
        // 验证昵称
        if (request.getNickname() == null || request.getNickname().trim().isEmpty()) {
            return Result.error("昵称不能为空");
        }
        
        // 验证手机号（如果提供了手机号）
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            String phone = request.getPhone().trim();
            // 中国大陆手机号正则：1开头，第二位3-9，后面9位数字
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                return Result.error("手机号格式不正确");
            }
            // 检查手机号是否已被注册
            if (userRepository.findByPhone(phone) != null) {
                return Result.error("该手机号已被注册");
            }
        }
        
        // 检查账号是否已存在
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
        // 验证输入
        if (request.getAccount() == null || request.getAccount().trim().isEmpty()) {
            return Result.error("账号不能为空");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return Result.error("密码不能为空");
        }
        
        User user = null;
        String account = request.getAccount().trim();
        
        // 判断是手机号还是账号
        if (account.matches("^1[3-9]\\d{9}$")) {
            // 手机号登录
            user = userRepository.findByPhone(account);
            if (user == null) {
                return Result.error("该手机号未注册");
            }
        } else {
            // 账号登录
            user = userRepository.findByAccount(account);
            if (user == null) {
                return Result.error("账号不存在");
            }
        }
        
        // 验证密码
        String md5Password = DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!user.getPassword().equals(md5Password)) {
            return Result.error("密码错误");
        }
        
        // 检查账号状态
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

    @Override
    public Result listUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userRepository.findAll(pageable);
        // 隐藏所有用户的密码
        userPage.getContent().forEach(user -> user.setPassword(null));
        return Result.success(userPage);
    }

    @Override
    public Result updateUserStatus(Integer userId, Integer status) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (status != 0 && status != 1) {
            return Result.error("状态值无效，必须为0(正常)或1(禁用)");
        }
        user.setStatus(status);
        userRepository.save(user);
        return Result.success("状态更新成功");
    }

    @Override
    public Result listAllUser() {
        List<User> userList = userRepository.findAll();
        // 隐藏密码，保证安全
        userList.forEach(u -> u.setPassword(null));
        return Result.success(userList);
    }

    @Override
    public Result deleteUser(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return Result.error("用户不存在");
        }
        userRepository.deleteById(userId);
        return Result.success("删除成功");
    }
}