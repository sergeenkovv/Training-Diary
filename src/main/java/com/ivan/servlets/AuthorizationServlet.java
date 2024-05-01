package com.ivan.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dto.ExceptionResponse;
import com.ivan.dto.JwtResponse;
import com.ivan.dto.SecurityRequest;
import com.ivan.exception.AuthorizationException;
import com.ivan.service.SecurityService;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/auth/authorization")
public class AuthorizationServlet extends HttpServlet {

    private SecurityService securityService;
    private ObjectMapper jacksonMapper;

    @Override
    public void init() {
        securityService = (SecurityService) getServletContext().getAttribute("securityService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (ServletInputStream inputStream = req.getInputStream()) {
            SecurityRequest securityRequest = jacksonMapper.readValue(inputStream, SecurityRequest.class);
            JwtResponse response = securityService.authorization(securityRequest.login(), securityRequest.password());

            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            jacksonMapper.writeValue(resp.getWriter(), response);
        } catch (JsonParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (AuthorizationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}