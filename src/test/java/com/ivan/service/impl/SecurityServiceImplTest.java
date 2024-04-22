package com.ivan.service.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.RegistrationException;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private AuditService auditService;

    private Athlete athlete;

    @BeforeEach
    void setup() {
        athlete = Athlete.builder()
                .id(1L)
                .login("Ivan")
                .password("1234")
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

        Athlete result = securityServiceImpl.authorization(athlete.getLogin(), athlete.getPassword());

        assertThat(result).isEqualTo(athlete);
    }

    @DisplayName("Test authorization method with incorrect password")
    @Test
    void authorization_WithIncorrectPassword_ThrowsAuthorizationException() {
        when(athleteDao.findByLogin(athlete.getLogin())).thenReturn(Optional.of(athlete));

        assertThatThrownBy(() -> securityServiceImpl.authorization(athlete.getLogin(), "incorrectPassword"))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("Incorrect password.");
    }

    @DisplayName("Test authorization method with non existing athlete")
    @Test
    void authorization_WithNonExistingAthlete_ThrowsAuthorizationException() {
        when(athleteDao.findByLogin("nonExistingLogin")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> securityServiceImpl.authorization("nonExistingLogin", "password"))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("There is no athlete with this login in the database!");
    }
}