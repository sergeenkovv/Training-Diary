package com.ivan.service.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.exception.TrainingLimitExceededException;
import com.ivan.exception.TrainingNotFoundException;
import com.ivan.model.Training;
import com.ivan.service.TrainingService;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDao;
    private final TrainingTypeService trainingTypeService;

    @Override
    public void addTraining(Long athleteId, String trainingType, Integer setsAmount, LocalDate date) {
        if (!isValidTraining(athleteId, date)) {
            throw new TrainingLimitExceededException("You cannot do one type of training more than once a day!");
        }

        Training training = Training.builder()
                .trainingType(trainingTypeService.getByTypeName(trainingType))
                .setsAmount(setsAmount)
                .date(date)
                .athleteId(athleteId)
                .build();

        trainingDao.save(training);
    }

    @Override
    public void updateTraining(Long athleteId, LocalDate date, String trainingType, String setsAmount) {
        Optional<Training> trainingOptional = trainingDao.findByAthleteIdAndTrainingDate(athleteId, date);

        if (trainingOptional.isEmpty()) {
            throw new TrainingNotFoundException("Training not found!");
        }

        Training existingTraining = trainingOptional.get();
        existingTraining.setTrainingType(trainingTypeService.getByTypeName(trainingType));
        existingTraining.setSetsAmount(Integer.parseInt(setsAmount));
    }

    @Override
    public List<Training> getTrainingsSortedByDate(Long athleteId) {
        List<Training> allByAthleteId = trainingDao.findAllByAthleteId(athleteId);
        allByAthleteId.sort(Comparator.comparing(Training::getDate));
        return allByAthleteId;
    }

    @Override
    public List<Training> getTrainingsBySetsAmount(Long athleteId) {
        List<Training> allByAthleteId = trainingDao.findAllByAthleteId(athleteId);
        allByAthleteId.sort(Comparator.comparingInt(Training::getSetsAmount).reversed());
        return allByAthleteId;
    }

    @Override
    public void deleteTraining(Long athleteId, LocalDate date) {
        Optional<Training> trainingOptional = trainingDao.findByAthleteIdAndTrainingDate(athleteId, date);

        if (trainingOptional.isEmpty()) {
            throw new TrainingNotFoundException("Training not found!");
        }

        Training trainingToDelete = trainingOptional.get();
        trainingDao.delete(trainingToDelete);
    }

    private boolean isValidTraining(Long athleteId, LocalDate date) {
        List<Training> allTrainingByLogin = trainingDao.findAllByAthleteId(athleteId);

        return allTrainingByLogin.stream()
                .noneMatch(training -> training.getDate().equals(date));
    }
}