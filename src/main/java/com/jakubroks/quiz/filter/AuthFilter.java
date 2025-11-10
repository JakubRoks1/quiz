package com.jakubroks.quiz.filter;

import com.jakubroks.quiz.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import jakarta.servlet.Filter;
    @Component
    public class AuthFilter implements Filter {

        private final UserService userService;

        public AuthFilter(UserService userService) {
            this.userService = userService;
        }

        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;

            String path = req.getRequestURI();

            if (path.startsWith("/auth") || path.startsWith("/public")) {
                chain.doFilter(request, response);
                return;
            }

            String key = req.getHeader("X-KEY");

            if (key == null || key.isBlank()) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("""
                        {"message":"Missing X-KEY"}
                        """);
                return;
            }

            userService.getByKey(key).ifPresentOrElse(user -> {
                req.setAttribute("user", user);

                try {
                    chain.doFilter(request, response);
                } catch (IOException | ServletException e) {
                    throw new RuntimeException(e);
                }

            }, () -> {
                try {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().write("""
        {"message":"Invalid or expired key"}
        """);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

