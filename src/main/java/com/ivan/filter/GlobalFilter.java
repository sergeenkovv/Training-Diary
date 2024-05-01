package com.ivan.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

/**
 * This filter is used to set the character encoding and content type for all requests and responses.
 * It sets the character encoding to UTF-8 and the content type to application/json.
 * This ensures that all requests and responses are properly encoded and formatted.
 * <p>
 * This filter extends {@link Filter}.
 *
 * @author sergeenkovv
 */
@WebFilter
public class GlobalFilter implements Filter {

    /**
     * Sets the character encoding to UTF-8 and the content type to application/json for the given request and response.
     *
     * @param request  the request to be filtered
     * @param response the response to be filtered
     * @param chain    the filter chain
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        chain.doFilter(request, response);
    }
}