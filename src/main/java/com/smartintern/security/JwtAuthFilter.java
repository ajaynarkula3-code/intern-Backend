package com.smartintern.security;

import com.smartintern.model.Role;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class JwtAuthFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            if (jwtUtil.validateToken(token)) {
                try {
                    Long userId = jwtUtil.getUserId(token);
                    String email = jwtUtil.getEmail(token);
                    String roleStr = jwtUtil.getRole(token);
                    Role role = roleStr != null ? Role.valueOf(roleStr) : Role.STUDENT;

                    UserContext.set(userId, email, role);
                    request.setAttribute("authenticatedUser", UserContext.get());
                } catch (Exception e) {
                    // Token parse error
                    UserContext.clear();
                }
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
