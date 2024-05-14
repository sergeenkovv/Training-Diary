package com.ivan.service.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.dto.JwtResponse;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.RegistrationException;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("securityServiceImpl implementation test")
@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @InjectMocks
    private SecurityServiceImpl securityServiceImpl;

    @Mock
    private AthleteDao athleteDao;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    private Athlete athlete;

    @BeforeEach
    void setup() {
        athlete = Athlete.builder()
                .id(1L)
                .login("Ivan")
                .password(passwordEncoder.encode("1234"))
                .role(Role.CLIENT)
                .build();
    }

    @DisplayName("Test registration method")
    @Test
    void registration_Success() {
        when(athleteDao.findByLogin(athlete.getLogin())).thenReturn(Optional.empty());
        when(athleteDao.save(any(Athlete.class))).thenReturn(athlete);

        Athlete registerAthlete = securityServiceImpl.registration(athlete.getLogin(), athlete.getPassword());
        assertEquals(athlete.getLogin(), registerAthlete.getLogin());
        assertEquals(athlete.getPassword(), registerAthlete.getPassword());
    }

    @DisplayName("Test registration method with exception")
    @Test
    void registration_RegistrationException() {
        when(athleteDao.findByLogin(athlete.getLogin())).thenReturn(Optional.of(athlete));

        assertThrows(RegistrationException.class,
                () -> securityServiceImpl.registration(athlete.getLogin(), athlete.getPassword()));
    }

    @DisplayName("Test authorization method")
    @Test
    void authorization_Success() {
        when(athleteDao.findByLogin(athlete.getLogin())).thenReturn(Optional.of(athlete));
        when(jwtTokenProvider.createAccessToken(athlete.getLogin())).thenReturn("testAccessToken");

        JwtResponse result = securityServiceImpl.authorization(athlete.getLogin(), athlete.getPassword());

        assertEquals(athlete.getLogin(), result.login());
        assertEquals("testAccessToken", result.accessToken());
    }

    @DisplayName("Test authorization method with non existing athlete")
    @Test
    void authorization_WithNonExistingAthlete_ThrowsAuthorizationException() {
        when(athleteDao.findByLogin("nonExistingLogin")).thenReturn(Optional.empty());

        assertThrows(AuthorizationException.class,
                () -> securityServiceImpl.authorization("nonExistingLogin", "password"));
    }
}