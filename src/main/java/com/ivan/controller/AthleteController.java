package com.ivan.controller;

import com.ivan.exception.NotValidArgumentException;
import com.ivan.exception.TrainingLimitExceededException;
import com.ivan.model.TrainingType;
import com.ivan.service.TrainingService;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AthleteController {

    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    public void addTraining(String trainingType, String setsAmount) {
        if (!isTrainingTypeValid(trainingType)) {
            throw new TrainingLimitExceededException("Select an available training!");
        }
        if (!isValidNum(setsAmount)) {
            throw new NotValidArgumentException("Enter a number greater than 0. letters cannot be used!");
        }

        trainingService.addTraining(trainingType, Integer.parseInt(setsAmount));
    }

    public List<TrainingType> showAvailableTrainingTypes() {
        return trainingTypeService.showAvailableTrainingTypes();
    }

    private boolean isTrainingTypeValid(String type) {
        return showAvailableTrainingTypes().stream()
                .anyMatch(trainingType -> trainingType.getTypeName().equals(type));
    }

    private boolean isValidNum(String... inputs) {
        return Arrays.stream(inputs)
                .allMatch(input -> input.matches("\\d+"));
    }
}
