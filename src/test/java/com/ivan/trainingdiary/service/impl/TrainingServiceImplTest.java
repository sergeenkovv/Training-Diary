package com.ivan.trainingdiary.service.impl;

import com.ivan.trainingdiary.exception.InvalidTrainingTypeException;
import com.ivan.trainingdiary.exception.TrainingLimitExceededException;
import com.ivan.trainingdiary.exception.TrainingNotFoundException;
import com.ivan.trainingdiary.model.Athlete;
import com.ivan.trainingdiary.model.Role;
import com.ivan.trainingdiary.model.Training;
import com.ivan.trainingdiary.model.TrainingType;
import com.ivan.trainingdiary.repository.TrainingRepository;
import com.ivan.trainingdiary.repository.TrainingTypeRepository;
import com.ivan.trainingdiary.service.AthleteService;
import com.ivan.trainingdiary.service.TrainingTypeService;
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
    private TrainingRepository trainingDao;
    @Mock
    private TrainingTypeRepository trainingTypeDao;
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private AthleteService athleteService;

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
                .trainingType(trainingType)
                .athlete(athlete)
                .build();

        training2 = Training.builder()
                .id(2L)
                .setsAmount(8)
                .date(LocalDate.parse("2022-11-11"))
                .trainingType(trainingType)
                .athlete(athlete)
                .build();

        training3 = Training.builder()
                .id(3L)
                .setsAmount(5)
                .date(LocalDate.parse("2020-11-11"))
                .trainingType(trainingType)
                .athlete(athlete)
                .build();
    }

    @DisplayName("Test addTraining method")
    @Test
    void addTraining_Success() {
        when(trainingDao.findByAthleteIdAndTrainingDate(anyLong(), any(LocalDate.class))).thenReturn(Optional.empty());
        when(athleteService.getById(training1.getAthlete().getId())).thenReturn(new Athlete());
        when(trainingTypeService.getByTypeName(training1.getTrainingType().getTypeName())).thenReturn(trainingType);

        trainingService.addTraining(training1.getAthlete().getId(), training1.getTrainingType().getTypeName(), training1.getSetsAmount());

        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @DisplayName("Test addTraining method with exception")
    @Test
    void addTraining_TrainingLimitExceededException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(training1.getId(), LocalDate.now())).thenReturn(Optional.ofNullable(training1));

        assertThrows(TrainingLimitExceededException.class,
                () -> trainingService.addTraining(training1.getAthlete().getId(), training1.getTrainingType().getTypeName(), training1.getSetsAmount()));
    }

    @DisplayName("Test addTraining method with exception")
    @Test
    void addTraining_InvalidTrainingTypeException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(training1.getId(), LocalDate.now())).thenReturn(Optional.ofNullable(training1));
        when(trainingTypeService.getByTypeName(anyString()))
                .thenThrow(new InvalidTrainingTypeException("Such type of training does not exist!"));

        assertThrows(InvalidTrainingTypeException.class,
                () -> trainingService.addTraining(training2.getAthlete().getId(), training1.getTrainingType().getTypeName(), training1.getSetsAmount()));
    }

    @DisplayName("Test editTraining method")
    @Test
    void editTraining_Success() {
        when(trainingTypeService.getByTypeName(trainingType.getTypeName())).thenReturn(trainingType);
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));

        trainingService.editTraining(athlete.getId(), training1.getDate(), trainingType.getTypeName(), 5);

        verify(trainingTypeService).getByTypeName(trainingType.getTypeName());
        verify(trainingDao).findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate());
        assertEquals(5, training1.getSetsAmount());
    }

    @DisplayName("Test editTraining method with exception")
    @Test
    void editTraining_InvalidTrainingTypeException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));
        when(trainingTypeService.getByTypeName(anyString()))
                .thenThrow(new InvalidTrainingTypeException("Such type of training does not exist!"));

        assertThrows(InvalidTrainingTypeException.class,
                () -> trainingService.editTraining(training1.getAthlete().getId(), training1.getDate(), training1.getTrainingType().getTypeName(), training1.getSetsAmount()));
    }

    @DisplayName("Test editTraining method with exception")
    @Test
    void editTraining_TrainingNotFoundException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(training1.getAthlete().getId(), LocalDate.now())).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class,
                () -> trainingService.editTraining(training1.getAthlete().getId(), LocalDate.now(), training1.getTrainingType().getTypeName(), training1.getSetsAmount()));
    }

    @DisplayName("Test getTrainingsSortedByDate method")
    @Test
    void getTrainingsSortedByDate_Success() {
        List<Training> unsortedTrainings = Arrays.asList(training3, training1, training2);
        List<Training> sortedTrainings = Arrays.asList(training1, training2, training3);

        when(trainingDao.findAllByAthleteId(training1.getAthlete().getId())).thenReturn(unsortedTrainings);

        List<Training> result = trainingService.getTrainingsSortedByDate(training1.getAthlete().getId());

        assertThat(result).isNotNull().hasSize(3).containsExactlyElementsOf(sortedTrainings);
    }

    @DisplayName("Test getTrainingsSortedBySetsAmount method")
    @Test
    void getTrainingsSortedBySetsAmount_Success() {
        List<Training> unsortedTrainings = Arrays.asList(training3, training1, training2);
        List<Training> sortedTrainings = Arrays.asList(training1, training2, training3);

        when(trainingDao.findAllByAthleteId(training1.getAthlete().getId())).thenReturn(unsortedTrainings);

        List<Training> result = trainingService.getTrainingsSortedByDate(training1.getAthlete().getId());

        assertThat(result).isNotNull().hasSize(3).containsExactlyElementsOf(sortedTrainings);
    }

    @DisplayName("Test deleteTraining method")
    @Test
    void deleteTraining_Success() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));

        trainingService.deleteTraining(training1.getAthlete().getId(), training1.getDate());

        verify(trainingDao).delete(training1.getId());
    }

    @DisplayName("Test deleteTraining method with exception")
    @Test
    void deleteTraining_TrainingNotFoundException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class,
                () -> trainingService.deleteTraining(training1.getAthlete().getId(), training1.getDate()));
    }

    @DisplayName("Test getTrainingByAthleteIdAndDate method")
    @Test
    void getTrainingByAthleteIdAndDate_Success() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.of(training1));

        Training result = trainingService.getTrainingByAthleteIdAndDate(training1.getAthlete().getId(), training1.getDate());

        assertEquals(training1, result);
    }

    @DisplayName("Test getTrainingByAthleteIdAndDate method with exception")
    @Test
    void getTrainingByAthleteIdAndDate_TrainingNotFoundException() {
        when(trainingDao.findByAthleteIdAndTrainingDate(athlete.getId(), training1.getDate())).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class,
                () -> trainingService.getTrainingByAthleteIdAndDate(training1.getAthlete().getId(), training1.getDate()));
    }
}