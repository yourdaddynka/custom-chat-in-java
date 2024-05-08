package org.example.security.jwt;


import org.example.model.dto.SenderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Извлечение токена из запроса
            String token = extractTokenFromRequest((HttpServletRequest) httpServletRequest);
            if (token != null) {
                String role = jwtTokenProvider.getRoleFromToken(token);
                String userName = jwtTokenProvider.getUsernameFromToken(token);
                SenderDto user = new SenderDto(userName, role);
                if (user != null) {
                    // Создание объекта аутентификации с ролями пользователя
                    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + user.getRole());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    // Установка объекта аутентификации в контекст безопасности
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("User {} successfully authenticated with roles: {}" + user.getName() + " " + authorities);
                }
            }
            // Продолжение цепочки фильтров
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception ex) {
            messagingTemplate.convertAndSend("/topic/error", ex.getMessage());
            // Обработка ошибок аутентификации
            handleAuthenticationError((HttpServletResponse) httpServletResponse, ex);
        }
    }

    // Метод для обработки ошибок аутентификации
    private void handleAuthenticationError(HttpServletResponse response, Exception ex) throws IOException {
        logger.error("Authentication error: " + ex.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String errorMessage = "{\"result\": \"Failed\", \"message\": \"" + ex.getMessage() + "\", \"errorCode\": \"" + "401" + "\"}";
        response.getWriter().write(errorMessage);
    }

    // Метод для извлечения токена из заголовка запроса
    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }


}

