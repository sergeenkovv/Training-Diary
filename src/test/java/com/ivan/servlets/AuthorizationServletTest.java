package com.ivan.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dto.ExceptionResponse;
import com.ivan.dto.JwtResponse;
import com.ivan.dto.SecurityRequest;
import com.ivan.exception.AuthorizationException;
import com.ivan.service.SecurityService;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServletTest {

    @Mock
    private SecurityService securityService;
    @Mock
    private ObjectMapper jacksonMapper;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletInputStream inputStream;
    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private AuthorizationServlet authorizationServlet;

    SecurityRequest securityRequest;
    JwtResponse jwtResponse;

    @BeforeEach
    void setup() {
        securityRequest = new SecurityRequest("testUser", "testPass");
        jwtResponse = new JwtResponse("testUser", "accessToken");
    }

    @Test
    @DisplayName("doPost success")
    void doPost_Success() throws IOException {
        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, SecurityRequest.class)).thenReturn(securityRequest);
        when(securityService.authorization(securityRequest.login(), securityRequest.password())).thenReturn(jwtResponse);
        when(response.getWriter()).thenReturn(printWriter);

        authorizationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);
        verify(jacksonMapper).writeValue(printWriter, jwtResponse);
    }

    @Test
    @DisplayName("doPost returns error response when AuthorizationException is thrown")
    void doPost_AuthorizationException() throws IOException {
        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, SecurityRequest.class)).thenReturn(securityRequest);
        when(securityService.authorization(securityRequest.login(), securityRequest.password())).thenThrow(AuthorizationException.class);
        when(response.getWriter()).thenReturn(printWriter);

        authorizationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }

    @Test
    @DisplayName("doPost returns error response when RuntimeException is thrown")
    void doPost_RuntimeException() throws IOException {
        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, SecurityRequest.class)).thenReturn(securityRequest);
        when(securityService.authorization(securityRequest.login(), securityRequest.password())).thenThrow(RuntimeException.class);
        when(response.getWriter()).thenReturn(printWriter);

        authorizationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }
}