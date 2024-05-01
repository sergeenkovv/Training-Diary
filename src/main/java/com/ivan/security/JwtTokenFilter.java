package com.ivan.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(urlPatterns = "/*", initParams = @WebInitParam(name = "order", value = "1"))
public class JwtTokenFilter implements Filter {

    private JwtTokenProvider jwtTokenProvider;

    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) {
        this.servletContext = filterConfig.getServletContext();
        jwtTokenProvider = (JwtTokenProvider) servletContext.getAttribute("jwtTokenProvider");
    }

    // TODO: 29.04.2024 проблема. я могу войти под старыми токеном
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = getJWTFromRequest((HttpServletRequest) servletRequest);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.authentication(token);
                servletContext.setAttribute("authentication", authentication);
            } else {
                servletContext.setAttribute("authentication", new Authentication(null, null, false, "Bearer token is null or invalid!"));
            }
        } catch (RuntimeException e) {
            servletContext.setAttribute("authentication", new Authentication(null, null, false, e.getMessage()));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && !header.isBlank() && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}