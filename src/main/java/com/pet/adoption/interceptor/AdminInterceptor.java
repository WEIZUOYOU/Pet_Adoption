package com.pet.adoption.interceptor;

import com.pet.adoption.common.SessionUtils;
import com.pet.adoption.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        User user = SessionUtils.getUser(request.getSession());
        if (user == null || user.getRole() != 1) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"无管理员权限\"}");
            return false;
        }
        return true;
    }
}