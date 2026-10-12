package com.smartintern.security;

import com.smartintern.model.Role;

public class UserContext {

    private static final ThreadLocal<AuthenticatedUser> current = new ThreadLocal<>();

    public static class AuthenticatedUser {
        private final Long userId;
        private final String email;
        private final Role role;

        public AuthenticatedUser(Long userId, String email, Role role) {
            this.userId = userId;
            this.email = email;
            this.role = role;
        }

        public Long getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public Role getRole() {
            return role;
        }
    }

    public static void set(Long userId, String email, Role role) {
        current.set(new AuthenticatedUser(userId, email, role));
    }

    public static AuthenticatedUser get() {
        return current.get();
    }

    public static Long getUserId() {
        AuthenticatedUser user = current.get();
        return user != null ? user.getUserId() : null;
    }

    public static String getEmail() {
        AuthenticatedUser user = current.get();
        return user != null ? user.getEmail() : null;
    }

    public static Role getRole() {
        AuthenticatedUser user = current.get();
        return user != null ? user.getRole() : null;
    }

    public static void clear() {
        current.remove();
    }
}
