package com.smartintern.security;

import com.smartintern.model.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Allow pre-flight CORS requests
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole == null) {
            requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        }

        if (requireRole == null) {
            return true; // No restriction specified
        }

        UserContext.AuthenticatedUser user = UserContext.get();
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized: Missing or invalid authentication token\"}");
            return false;
        }

        Role[] allowedRoles = requireRole.value();
        if (allowedRoles.length > 0) {
            boolean hasPermission = Arrays.asList(allowedRoles).contains(user.getRole());
            if (!hasPermission) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Forbidden: Insufficient privileges for role " + user.getRole() + "\"}");
                return false;
            }
        }

        return true;
    }
}
