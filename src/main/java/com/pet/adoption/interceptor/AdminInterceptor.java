package com.pet.adoption.interceptor;

import com.pet.adoption.common.SessionUtils;
import com.pet.adoption.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = SessionUtils.getUser(request.getSession());
        if (user == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"请先登录\"}");
            return false;
        }
        // 检查角色：1为管理员
        if (user.getRole() == null || user.getRole() != 1) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"无权限访问\"}");
            return false;
        }
        return true;
    }
}