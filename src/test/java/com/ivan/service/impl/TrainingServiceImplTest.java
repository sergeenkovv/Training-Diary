package com.ivan.service.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.dao.TrainingTypeDao;
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
import org.junit.jupiter.api.DisplayName;
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

@DisplayName("trainingServiceImpl implementation test")
@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingDao trainingDao;
    @Mock
    private TrainingTypeDao trainingTypeDao;
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
                .setsAmount(3)
                .date(LocalDate.now())
                .typeId(trainingType.getId())
                .athleteId(athlete.getId())
                .build();

        training2 = Training.builder()
                .id(2L)
                .setsAmount(8)
                .date(LocalDate.parse("2022-11-11"))
                .typeId(trainingType.getId())
                .athleteId(athlete.getId())
                .build();

        training3 = Training.builder()
                .id(3L)
                .setsAmount(5)
                .date(LocalDate.parse("2020-11-11"))
                .typeId(trainingType.getId())
                .athleteId(athlete.getId())
                .build();
    }

    @DisplayName("Test addTraining method")
    @Test
    void addTraining_Success() {
        when(trainingDao.findByAthleteIdAndTrainingDate(anyLong(), any(LocalDate.class))).thenReturn(Optional.empty());
        when(athleteService.getAthleteByAthleteId(training1.getAthleteId())).thenReturn(new Athlete());
        when(trainingTypeService.getByTypeId(training1.getTypeId())).thenReturn(trainingType);

        trainingService.addTraining(training1.getAthleteId(), training1.getTypeId(), training1.getSetsAmount());

        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @DisplayName("Test addTraining method with exception")
    @Test
    void addTraining_TrainingLimitExceededException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(training1.getId(), LocalDate.now())).thenReturn(Optional.ofNullable(training1));

        assertThrows(TrainingLimitExceededException.class,
                () -> trainingService.addTraining(training1.getAthleteId(), training1.getTypeId(), training1.getSetsAmount()));
    }

    @DisplayName("Test addTraining method with exception")
    @Test
    void addTraining_InvalidTrainingTypeException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(training1.getId(), LocalDate.now())).thenReturn(Optional.ofNullable(training1));
        when(trainingTypeService.getByTypeId(anyLong()))
                .thenThrow(new InvalidTrainingTypeException("Such type of training does not exist!"));

        assertThrows(InvalidTrainingTypeException.class,
                () -> trainingService.addTraining(training1.getAthleteId(), training1.getTypeId(), training1.getSetsAmount()));
    }

    @DisplayName("Test editTraining method")
    @Test
    void editTraining_Success() {
        when(trainingTypeService.getByTypeId(trainingType.getId())).thenReturn(trainingType);
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));
        when(athleteService.getAthleteByAthleteId(training1.getId())).thenReturn(athlete);

        trainingService.editTraining(athlete.getId(), training1.getDate(), trainingType.getId(), "5");

        verify(trainingTypeService).getByTypeId(trainingType.getId());
        verify(trainingDao).findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate());
        assertEquals(5, training1.getSetsAmount());
    }

    @DisplayName("Test editTraining method with exception")
    @Test
    void editTraining_InvalidTrainingTypeException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));
        when(trainingTypeService.getByTypeId(anyLong()))
                .thenThrow(new InvalidTrainingTypeException("Such type of training does not exist!"));

        assertThrows(InvalidTrainingTypeException.class,
                () -> trainingService.editTraining(training1.getAthleteId(), training1.getDate(), training1.getTypeId(), String.valueOf(training1.getSetsAmount())));
    }

    @DisplayName("Test editTraining method with exception")
    @Test
    void editTraining_TrainingNotFoundException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(training1.getAthleteId(), LocalDate.now())).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class,
                () -> trainingService.editTraining(training1.getAthleteId(), LocalDate.now(), training1.getTypeId(), String.valueOf(training1.getSetsAmount())));
    }

    @DisplayName("Test getTrainingsSortedByDate method")
    @Test
    void getTrainingsSortedByDate_Success() {
        List<Training> unsortedTrainings = Arrays.asList(training3, training1, training2);
        List<Training> sortedTrainings = Arrays.asList(training3, training2, training1);

        when(athleteService.getAthleteByAthleteId(training1.getAthleteId())).thenReturn(athlete);
        when(trainingDao.findAllByAthleteId(training1.getAthleteId())).thenReturn(unsortedTrainings);

        List<Training> result = trainingService.getTrainingsSortedByDate(training1.getAthleteId());

        assertThat(result).isNotNull().hasSize(3).containsExactlyElementsOf(sortedTrainings);
    }

    @DisplayName("Test getTrainingsSortedBySetsAmount method")
    @Test
    void getTrainingsSortedBySetsAmount_Success() {
        List<Training> unsortedTrainings = Arrays.asList(training3, training1, training2);
        List<Training> sortedTrainings = Arrays.asList(training2, training3, training1);


        when(athleteService.getAthleteByAthleteId(training1.getAthleteId())).thenReturn(athlete);
        when(trainingDao.findAllByAthleteId(training1.getAthleteId())).thenReturn(unsortedTrainings);

        List<Training> result = trainingService.getTrainingsSortedBySetsAmount(training1.getAthleteId());

        assertThat(result).isNotNull().hasSize(3).containsExactlyElementsOf(sortedTrainings);
    }

    @DisplayName("Test deleteTraining method")
    @Test
    void deleteTraining_Success() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));
        when(athleteService.getAthleteByAthleteId(training1.getId())).thenReturn(athlete);

        trainingService.deleteTraining(training1.getAthleteId(), training1.getDate());

        verify(trainingDao).delete(training1.getId());
    }

    @DisplayName("Test deleteTraining method with exception")
    @Test
    void deleteTraining_TrainingNotFoundException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class,
                () -> trainingService.deleteTraining(training1.getAthleteId(), training1.getDate()));
    }

    @DisplayName("Test getTrainingByAthleteIdAndDate method")
    @Test
    void getTrainingByAthleteIdAndDate_Success() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));

        Training result = trainingService.getTrainingByAthleteIdAndDate(training1.getAthleteId(), training1.getDate());

        assertEquals(training1, result);
    }

    @DisplayName("Test getTrainingByAthleteIdAndDate method with exception")
    @Test
    void getTrainingByAthleteIdAndDate_TrainingNotFoundException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class,
                () -> trainingService.getTrainingByAthleteIdAndDate(training1.getAthleteId(), training1.getDate()));
    }
}