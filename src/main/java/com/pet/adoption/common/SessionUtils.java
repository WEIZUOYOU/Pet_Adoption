package com.pet.adoption.common;

import com.pet.adoption.entity.User;
import javax.servlet.http.HttpSession;

public class SessionUtils {
    public static final String USER_KEY = "loginUser";

    public static void setUser(HttpSession session, User user) {
        session.setAttribute(USER_KEY, user);
    }

    public static User getUser(HttpSession session) {
        return (User) session.getAttribute(USER_KEY);
    }

    public static void removeUser(HttpSession session) {
        session.removeAttribute(USER_KEY);
    }
}