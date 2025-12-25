package com.pocketsiem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyAuthenticationFilter.class);
    private static final String API_KEY_HEADER = "X-API-KEY";

    @Value("${api.key:pocketsiem-demo-key-12345}")
    private String validApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        if (isPublicEndpoint(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("Missing API key for endpoint: {}", requestPath);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing X-API-KEY header");
            return;
        }

        if (!apiKey.equals(validApiKey)) {
            log.warn("Invalid API key attempt for endpoint: {}", requestPath);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid API key");
            return;
        }

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken("api-client", null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path) {
        return path.contains("/h2-console") ||
               path.contains("/actuator") ||
               path.equals("/api/v1/health");
    }
}
