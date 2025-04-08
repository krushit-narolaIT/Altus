package com.krushit.utils;

import com.krushit.model.User;

public class UserContextUtils {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

/*    public UserContextUtils(User user) {
        currentUser = user;
    }*/

    public static User getUser() {
        return currentUser.get();
    }

    public static void setUser(User user) {
        currentUser.set(user);
    }

    public static void clear() {
        currentUser.remove();
    }
}
