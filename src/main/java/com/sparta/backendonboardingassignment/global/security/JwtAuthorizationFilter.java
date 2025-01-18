package com.sparta.backendonboardingassignment.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String errorMessage = "토큰을 재발급해주세요.";

        try {
            String tokenValue = jwtUtil.getAccessTokenFromHeader(req);

            if (tokenValue == null) {
                errorMessage = "재로그인을 해주세요.";
                tokenValue = jwtUtil.getRefreshTokenFromHeader(req);
            }

            if (StringUtils.hasText(tokenValue)) {
                if (!jwtUtil.validateToken(tokenValue)) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    sendMessage(res, errorMessage);
                    return;
                }

                Claims userInfo = jwtUtil.getUserInfoFromToken(tokenValue);

                try {
                    setAuthentication(userInfo.getSubject());
                } catch (Exception e) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    sendMessage(res, errorMessage);
                    return;
                }
            }
            filterChain.doFilter(req, res);
        } catch (UnsupportedJwtException e) {
            sendMessage(res, errorMessage);
        }

    }

    public void setAuthentication(String username) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void sendMessage(HttpServletResponse res, String message) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        res.setContentType("application/json;charset=UTF-8");
        String jsonResponse = objectMapper.writeValueAsString(Map.of("statusCode", res.getStatus(), "msg", message));

        res.getWriter().write(jsonResponse);
    }
}
