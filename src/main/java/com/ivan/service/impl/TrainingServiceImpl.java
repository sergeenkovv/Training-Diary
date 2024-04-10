package com.ivan.service.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.exception.TrainingLimitExceededException;
import com.ivan.model.Training;
import com.ivan.service.TrainingService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDao;

    @Override
    public void addTraining(String trainingType, Integer setsAmount) {
        if (!isValidTraining()) {
            throw new TrainingLimitExceededException("You cannot do one type of training more than once a day!");
        }

        Training training = Training.builder()
                .trainingType(trainingType)
                .setsAmount(setsAmount)
                .date(YearMonth.now())
                .build();

        trainingDao.save(training);
    }

    @Override
    public Training editTraining() {
        return null; // TODO: 11.04.2024  изменять будем по тренировка вводя idшник. потому что у нас может быть несколько тренировок за день
    }

    @Override
    public List<Training> getTrainingsSortedByDate() {
        return null;
    }

    private boolean isValidTraining() {
// TODO: 11.04.2024 тренировки одного типа можно заносить 1 раз в день
        return true;
    }
}
