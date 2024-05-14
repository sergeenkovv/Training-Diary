package com.ivan.controller;

import com.ivan.dto.AthleteResponse;
import com.ivan.dto.JwtResponse;
import com.ivan.dto.SecurityRequest;
import com.ivan.mapper.AthleteMapper;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityControllerTest {

    @InjectMocks
    private SecurityController securityController;

    @Mock
    private SecurityService securityService;
    @Mock
    private AthleteMapper playerMapper;

    @Test
    public void testRegister() throws Exception {
        final String login = "test";
        final String password = "test";
        final SecurityRequest request = new SecurityRequest(login, password);
        Athlete player = new Athlete(1L, login, password, Role.CLIENT);
        AthleteResponse athleteResponse = new AthleteResponse(login, Role.CLIENT);
        when(securityService.registration(login, password)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(athleteResponse);

        ResponseEntity<?> response = securityController.registration(request);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        AthleteResponse body = (AthleteResponse) response.getBody();

        assertNotNull(body);
        assertEquals(athleteResponse.login(), body.login());
        assertEquals(athleteResponse.role().getAuthority(), body.role().getAuthority());
    }

    @Test
    public void testAuthorize() {
        final String login = "test";
        final String password = "test";
        final SecurityRequest request = new SecurityRequest(login, password);
        JwtResponse jwt = new JwtResponse(login, "accessToken");
        when(securityService.authorization(request.login(), request.password())).thenReturn(jwt);
        ResponseEntity<?> response = securityController.authorize(request);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        JwtResponse body = (JwtResponse) response.getBody();

        assertNotNull(body);
        assertEquals(jwt, body);
    }
}