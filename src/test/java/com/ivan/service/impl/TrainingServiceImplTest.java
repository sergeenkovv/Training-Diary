package com.ivan.service.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.exception.InvalidTrainingTypeException;
import com.ivan.exception.TrainingLimitExceededException;
import com.ivan.exception.TrainingNotFoundException;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.model.Training;
import com.ivan.model.TrainingType;
import com.ivan.service.AthleteService;
import com.ivan.service.AuditService;
import com.ivan.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingDao trainingDao;
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private AthleteService athleteService;
    @Mock
    private AuditService auditService;

    Athlete athlete;
    Training training1;
    Training training2;
    Training training3;
    TrainingType trainingType;

    @BeforeEach
    void setUp() {
        athlete = Athlete.builder()
                .id(1L)
                .login("Ivan")
                .password("1234")
                .role(Role.CLIENT)
                .build();

        trainingType = TrainingType.builder()
                .id(1L)
                .typeName("LEGS")
                .build();

        training1 = Training.builder()
                .id(1L)
                .trainingType(trainingType)
                .setsAmount(3)
                .date(LocalDate.now())
                .athleteId(athlete.getId())
                .build();

        training2 = Training.builder()
                .id(2L)
                .trainingType(trainingType)
                .setsAmount(8)
                .date(LocalDate.parse("2022-11-11"))
                .athleteId(athlete.getId())
                .build();

        training3 = Training.builder()
                .id(3L)
                .trainingType(trainingType)
                .setsAmount(5)
                .date(LocalDate.parse("2020-11-11"))
                .athleteId(athlete.getId())
                .build();
    }

    @Test
    void addTraining_Success() {
        Long athleteId = 1L;
        String trainingType = "LEGS";
        Integer setsAmount = 3;

        when(trainingDao.findByAthleteIdAndTrainingDate(anyLong(), any(LocalDate.class))).thenReturn(Optional.empty());
        when(trainingTypeService.getByTypeName(trainingType)).thenReturn(new TrainingType());
        when(athleteService.getAthleteByAthleteId(athleteId)).thenReturn(new Athlete());

        trainingService.addTraining(athleteId, trainingType, setsAmount);

        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @Test
    void addTraining_TrainingLimitExceededException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(training1.getId(), LocalDate.now())).thenReturn(Optional.ofNullable(training1));

        assertThrows(TrainingLimitExceededException.class,
                () -> trainingService.addTraining(training1.getAthleteId(), training1.getTrainingType().getTypeName(), training1.getSetsAmount()));
    }

    @Test
    void editTraining_Success() {
        when(trainingTypeService.getByTypeName(trainingType.getTypeName())).thenReturn(trainingType);
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));
        when(athleteService.getAthleteByAthleteId(training1.getId())).thenReturn(athlete);

        trainingService.editTraining(athlete.getId(), training1.getDate(), trainingType.getTypeName(), "5");

        verify(trainingTypeService).getByTypeName(trainingType.getTypeName());
        verify(trainingDao).findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate());
        assertEquals(5, training1.getSetsAmount());
    }

    @Test
    void editTraining_InvalidTrainingTypeException() {
        when(trainingTypeService.getByTypeName(trainingType.getTypeName())).thenThrow(InvalidTrainingTypeException.class);

        assertThrows(InvalidTrainingTypeException.class,
                () -> trainingService.editTraining(training1.getAthleteId(), LocalDate.now(), trainingType.getTypeName(), String.valueOf(training1.getSetsAmount())));
    }

    @Test
    void editTraining_TrainingNotFoundException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(training1.getAthleteId(), LocalDate.now())).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class,
                () -> trainingService.editTraining(training1.getAthleteId(), LocalDate.now(), trainingType.getTypeName(), String.valueOf(training1.getSetsAmount())));
    }

    @Test
    void getTrainingsSortedByDate_Success() {
        List<Training> unsortedTrainings = Arrays.asList(training3, training1, training2);
        List<Training> sortedTrainings = Arrays.asList(training3, training2, training1);

        when(athleteService.getAthleteByAthleteId(training1.getAthleteId())).thenReturn(athlete);
        when(trainingDao.findAllByAthleteId(training1.getAthleteId())).thenReturn(unsortedTrainings);

        List<Training> result = trainingService.getTrainingsSortedByDate(training1.getAthleteId());

        assertThat(result).isNotNull().hasSize(3).containsExactlyElementsOf(sortedTrainings);
    }

    @Test
    void getTrainingsSortedBySetsAmount_Success() {
        List<Training> unsortedTrainings = Arrays.asList(training3, training1, training2);
        List<Training> sortedTrainings = Arrays.asList(training2, training3, training1);


        when(athleteService.getAthleteByAthleteId(training1.getAthleteId())).thenReturn(athlete);
        when(trainingDao.findAllByAthleteId(training1.getAthleteId())).thenReturn(unsortedTrainings);

        List<Training> result = trainingService.getTrainingsSortedBySetsAmount(training1.getAthleteId());

        assertThat(result).isNotNull().hasSize(3).containsExactlyElementsOf(sortedTrainings);
    }

    @Test
    void deleteTraining_Success() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));
        when(athleteService.getAthleteByAthleteId(training1.getId())).thenReturn(athlete);

        trainingService.deleteTraining(training1.getAthleteId(), training1.getDate());

        verify(trainingDao).delete(training1);
    }

    @Test
    void deleteTraining_TrainingNotFoundException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class,
                () -> trainingService.deleteTraining(training1.getAthleteId(), training1.getDate()));
    }

    @Test
    void getTrainingByAthleteIdAndDate_Success() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));

        Training result = trainingService.getTrainingByAthleteIdAndDate(training1.getAthleteId(), training1.getDate());

        assertEquals(training1, result);
    }

    @Test
    void getTrainingByAthleteIdAndDate_TrainingNotFoundException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class,
                () -> trainingService.getTrainingByAthleteIdAndDate(training1.getAthleteId(), training1.getDate()));
    }
}