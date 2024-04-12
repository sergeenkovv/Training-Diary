package com.ivan.service.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.exception.TrainingLimitExceededException;
import com.ivan.exception.TrainingNotFoundException;
import com.ivan.model.ActionType;
import com.ivan.model.Athlete;
import com.ivan.model.Training;
import com.ivan.service.AthleteService;
import com.ivan.service.AuditService;
import com.ivan.service.TrainingService;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.ivan.model.ActionType.ADD_TRAINING;

@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDao;
    private final TrainingTypeService trainingTypeService;
    private final AuditService auditService;
    private final AthleteService athleteService;

    @Override
    public void addTraining(Long athleteId, String trainingType, Integer setsAmount, LocalDate date) {
        Athlete athlete = getAthleteByAthleteId(athleteId);

        if (!isValidTraining(athleteId, date)) {
            throw new TrainingLimitExceededException("You cannot do one type of training more than once a day!");
        }

        Training training = Training.builder()
                .trainingType(trainingTypeService.getByTypeName(trainingType))
                .setsAmount(setsAmount)
                .date(date)
                .athleteId(athleteId)
                .build();

        auditService.audit(
                ADD_TRAINING, athlete.getLogin());

        trainingDao.save(training);
    }

    @Override
    public void updateTraining(Long athleteId, LocalDate date, String trainingType, String setsAmount) {
        Athlete athlete = getAthleteByAthleteId(athleteId);

        Optional<Training> trainingOptional = trainingDao.findByAthleteIdAndTrainingDate(athleteId, date);

        if (trainingOptional.isEmpty()) {
            throw new TrainingNotFoundException("Training not found!");
        }

        auditService.audit(
                ActionType.UPDATE_TRAINING, athlete.getLogin());

        Training existingTraining = trainingOptional.get();
        existingTraining.setTrainingType(trainingTypeService.getByTypeName(trainingType));
        existingTraining.setSetsAmount(Integer.parseInt(setsAmount));
    }

    @Override
    public List<Training> getTrainingsSortedByDate(Long athleteId) {
        Athlete athlete = getAthleteByAthleteId(athleteId);

        auditService.audit(
                ActionType.GET_TRAININGS_SORTED_BY_DATE, athlete.getLogin());

        List<Training> allByAthleteId = trainingDao.findAllByAthleteId(athleteId);
        allByAthleteId.sort(Comparator.comparing(Training::getDate));
        return allByAthleteId;
    }

    @Override
    public List<Training> getTrainingsSortedBySetsAmount(Long athleteId) {
        Athlete athlete = getAthleteByAthleteId(athleteId);

        auditService.audit(
                ActionType.GET_TRAININGS_SORTED_BY_SETS_AMOUNT, athlete.getLogin());

        List<Training> allByAthleteId = trainingDao.findAllByAthleteId(athleteId);
        allByAthleteId.sort(Comparator.comparingInt(Training::getSetsAmount).reversed());
        return allByAthleteId;
    }

    @Override
    public void deleteTraining(Long athleteId, LocalDate date) {
        Athlete athlete = getAthleteByAthleteId(athleteId);

        Optional<Training> trainingOptional = trainingDao.findByAthleteIdAndTrainingDate(athleteId, date);

        if (trainingOptional.isEmpty()) {
            throw new TrainingNotFoundException("Training not found!");
        }

        auditService.audit(
                ActionType.GET_TRAININGS_SORTED_BY_SETS_AMOUNT, athlete.getLogin());

        Training trainingToDelete = trainingOptional.get();
        trainingDao.delete(trainingToDelete);
    }

    private boolean isValidTraining(Long athleteId, LocalDate date) {
        List<Training> allTrainingByLogin = trainingDao.findAllByAthleteId(athleteId);

        return allTrainingByLogin.stream()
                .noneMatch(training -> training.getDate().equals(date));
    }

    private Athlete getAthleteByAthleteId(Long id) {
        return athleteService.getAthleteById(id)
                .orElseThrow(() -> new NoSuchElementException("No athlete found with ID: " + id));
    }
}