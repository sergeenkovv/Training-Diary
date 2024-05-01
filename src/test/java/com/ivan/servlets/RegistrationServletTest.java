package com.ivan.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dto.ExceptionResponse;
import com.ivan.dto.SecurityRequest;
import com.ivan.dto.SuccessResponse;
import com.ivan.exception.RegistrationException;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.service.SecurityService;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServletTest {

    @Mock
    private SecurityService securityService;

    @Mock
    private ObjectMapper jacksonMapper;

    @InjectMocks
    private RegistrationServlet registrationServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletInputStream inputStream;

    @Mock
    private PrintWriter printWriter;

    SecurityRequest securityRequest;
    Athlete athlete;

    @BeforeEach
    void setup() {
        securityRequest = new SecurityRequest("testUser", "testPass");

        athlete = Athlete.builder()
                .id(1L)
                .login("Ivan")
                .password("1234")
                .role(Role.CLIENT)
                .build();
    }

    @Test
    void doPost_Success() throws IOException {
        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, SecurityRequest.class)).thenReturn(securityRequest);
        when(securityService.registration(anyString(), anyString())).thenReturn(athlete);
        when(response.getWriter()).thenReturn(printWriter);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(SuccessResponse.class));
    }

    @Test
    void doPost_RegistrationException() throws IOException {
        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, SecurityRequest.class)).thenReturn(securityRequest);
        when(securityService.registration(securityRequest.login(), securityRequest.password())).thenThrow(RegistrationException.class);
        when(response.getWriter()).thenReturn(printWriter);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }

    @Test
    void doPost_RuntimeException() throws IOException {
        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, SecurityRequest.class)).thenReturn(securityRequest);
        when(securityService.registration(athlete.getLogin(), athlete.getPassword())).thenThrow(RuntimeException.class);
        when(response.getWriter()).thenReturn(printWriter);

        registrationServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(jacksonMapper).writeValue(any(PrintWriter.class), any(ExceptionResponse.class));
    }
}