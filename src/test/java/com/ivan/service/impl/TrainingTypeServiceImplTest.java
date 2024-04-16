package com.ivan.service.impl;

import com.ivan.dao.TrainingTypeDao;
import com.ivan.exception.InvalidTrainingTypeException;
import com.ivan.model.TrainingType;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeServiceImpl;

    @Mock
    private TrainingTypeDao trainingTypeDao;

    private List<TrainingType> mockTrainingTypes;

    TrainingType trainingType1;
    TrainingType trainingType2;

    @BeforeEach
    void setUp() {
        trainingType1 = TrainingType.builder()
                .id(1L)
                .typeName("CHEST")
                .build();

        trainingType2 = TrainingType.builder()
                .id(2L)
                .typeName("LEGS")
                .build();

        mockTrainingTypes = Arrays.asList(trainingType1, trainingType2);
    }

    @Test
    void getAvailableTrainingTypes_Success() {
        when(trainingTypeDao.findAll()).thenReturn(mockTrainingTypes);

        List<TrainingType> result = trainingTypeServiceImpl.getAllTrainingTypes();

        assertThat(result).isEqualTo(mockTrainingTypes);
    }

    @Test
    void addTrainingType_Success() {
        TrainingType newTrainingType = TrainingType.builder()
                .id(3L)
                .typeName("CARDIO")
                .build();

        trainingTypeServiceImpl.addTrainingType(newTrainingType);

        verify(trainingTypeDao).save(newTrainingType);
    }

    @Test
    void delete_Success() {
        when(trainingTypeDao.findByTypeName(trainingType1.getTypeName())).thenReturn(Optional.of(trainingType1));

        trainingTypeServiceImpl.deleteTrainingType(trainingType1.getTypeName());

        verify(trainingTypeDao, times(1)).delete(trainingType1);
    }

    @Test
    void delete_InvalidTrainingTypeException() {
        when(trainingTypeDao.findByTypeName(trainingType1.getTypeName())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingTypeServiceImpl.deleteTrainingType(trainingType1.getTypeName()))
                .isInstanceOf(InvalidTrainingTypeException.class)
                .hasMessage("Such type of training does not exist!");
    }

    @Test
    void getByTypeName_Success() {
        String typeName = "CHEST";
        TrainingType expectedTrainingType = TrainingType.builder()
                .id(3L)
                .typeName(typeName)
                .build();

        when(trainingTypeDao.findByTypeName(trainingType1.getTypeName())).thenReturn(Optional.of(expectedTrainingType));

        TrainingType result = trainingTypeServiceImpl.getByTypeName(typeName);

        assertThat(result).isEqualTo(expectedTrainingType);
    }

    @Test
    void getByTypeName_InvalidTrainingTypeException() {
        String typeName = "NonExistentType";
        when(trainingTypeDao.findByTypeName(typeName)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingTypeServiceImpl.getByTypeName(typeName))
                .isInstanceOf(InvalidTrainingTypeException.class)
                .hasMessage("Such type of training does not exist!");
    }
}