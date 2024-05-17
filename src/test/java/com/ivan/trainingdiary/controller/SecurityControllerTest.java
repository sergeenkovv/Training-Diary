package com.ivan.trainingdiary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.trainingdiary.dto.AthleteResponse;
import com.ivan.trainingdiary.dto.JwtResponse;
import com.ivan.trainingdiary.dto.SecurityRequest;
import com.ivan.trainingdiary.mapper.AthleteMapper;
import com.ivan.trainingdiary.model.Athlete;
import com.ivan.trainingdiary.model.Role;
import com.ivan.trainingdiary.security.JwtTokenProvider;
import com.ivan.trainingdiary.service.SecurityService;
import com.ivan.trainingdiary.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = SecurityController.class)
class SecurityControllerTest {

    @MockBean
    private SecurityService securityService;
    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @MockBean
    private AthleteMapper athleteMapper;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @Autowired
    private MockMvc mockMvc;

    private Athlete athlete;
    private AthleteResponse athleteResponse;
    private JwtResponse jwtResponse;
    private SecurityRequest securityRequest;
    private ObjectMapper objectMapper;;

    @BeforeEach
    void setUp() {
        String login = "ivan";
        String password = "password";

        athlete = Athlete.builder()
                .id(1L)
                .login(login)
                .password(passwordEncoder.encode(password))
                .role(Role.CLIENT)
                .build();

        athleteResponse = new AthleteResponse(athlete.getLogin(), athlete.getRole());

        jwtResponse = new JwtResponse(athlete.getLogin(), "accessToken");

        securityRequest = new SecurityRequest(athlete.getLogin(), athlete.getPassword());

        objectMapper = new ObjectMapper();
    }

    @DisplayName("Test registration method")
    @Test
    void registration_Success() throws Exception {
        given(securityService.registration(anyString(), anyString())).willReturn(athlete);
        given(athleteMapper.toDto(athlete)).willReturn(athleteResponse);

        ResultActions perform = mockMvc.perform(post("/api/auth/registration")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(securityRequest))
                .accept(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(athleteResponse)));
    }
    @DisplayName("Test authorization method")

    @Test
    void authorization_Success() throws Exception {
        given(securityService.authorization(anyString(), anyString())).willReturn(jwtResponse);

        ResultActions perform = mockMvc.perform(post("/api/auth/authorization")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(securityRequest))
                .accept(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }
}