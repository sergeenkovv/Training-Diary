package com.ivan.service.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.exception.AthleteNotFoundException;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AthleteServiceImplTest {

    @InjectMocks
    private AthleteServiceImpl athleteService;

    @Mock
    private AthleteDao athleteDao;

    private Athlete athlete1;
    private Athlete athlete2;

    @BeforeEach
    void setup() {
        athlete1 = Athlete.builder()
                .id(1L)
                .login("Ivan")
                .password("1234")
                .role(Role.CLIENT)
                .build();

        athlete2 = Athlete.builder()
                .id(2L)
                .login("Lesha")
                .password("4321")
                .role(Role.CLIENT)
                .build();
    }

    @Test
    void showAllAthlete_Success() {
        List<Athlete> athletes = Arrays.asList(athlete1, athlete2);

        when(athleteDao.findAll()).thenReturn(athletes);

        List<Athlete> result = athleteService.showAllAthletes();

        assertThat(result).containsExactlyInAnyOrder(athlete1, athlete2);
    }

    @Test
    void getAthleteById_Success() {
        when(athleteDao.findById(athlete1.getId())).thenReturn(Optional.of(athlete1));

        Athlete result = athleteService.getAthleteByAthleteId(athlete1.getId());

        assertThat(result).isEqualTo(athlete1);
    }

    @Test
    void getAthleteById_ThrowsAthleteNotFoundException() {
        Long nonExistingId = 100L;
        when(athleteDao.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> athleteService.getAthleteByAthleteId(nonExistingId))
                .isInstanceOf(AthleteNotFoundException.class)
                .hasMessage("No athlete found with ID: " + nonExistingId);
    }
}