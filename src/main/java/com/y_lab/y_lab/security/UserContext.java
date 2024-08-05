package com.y_lab.y_lab.security;

import com.y_lab.y_lab.entity.UserInfo;

public class UserContext {
    private static final ThreadLocal<UserInfo> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(UserInfo userInfo) {
        currentUser.set(userInfo);
    }

    public static UserInfo getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}
