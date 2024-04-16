package com.ivan.service.impl;

import com.ivan.dao.TrainingDao;
import com.ivan.exception.TrainingLimitExceededException;
import com.ivan.exception.TrainingNotFoundException;
import com.ivan.model.ActionType;
import com.ivan.model.Athlete;
import com.ivan.model.Training;
import com.ivan.model.TrainingType;
import com.ivan.service.AthleteService;
import com.ivan.service.AuditService;
import com.ivan.service.TrainingService;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.ivan.model.ActionType.ADD_TRAINING;

@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDao;
    private final TrainingTypeService trainingTypeService;
    private final AuditService auditService;
    private final AthleteService athleteService;

    @Override
    public void addTraining(Long athleteId, String trainingType, Integer setsAmount) {
        Optional<Training> maybeTraining = trainingDao.findByAthleteIdAndTrainingDate(athleteId, LocalDate.now());

        if (maybeTraining.isPresent()) {
            throw new TrainingLimitExceededException("You cannot do one type of training more than once a day!");
        }

        Training training = Training.builder()
                .trainingType(trainingTypeService.getByTypeName(trainingType))
                .setsAmount(setsAmount)
                .date(LocalDate.now())
                .athleteId(athleteId)
                .build();

        Athlete athlete = athleteService.getAthleteByAthleteId(athleteId);
        auditService.audit(
                ADD_TRAINING, athlete.getLogin());

        trainingDao.save(training);
    }

    @Override
    public void editTraining(Long athleteId, LocalDate date, String trainingType, String setsAmount) {
        TrainingType byTypeName = trainingTypeService.getByTypeName(trainingType);
        Training existingTraining = getTrainingByAthleteIdAndDate(athleteId, date);

        existingTraining.setTrainingType(byTypeName);
        existingTraining.setSetsAmount(Integer.parseInt(setsAmount));

        Athlete athlete = athleteService.getAthleteByAthleteId(athleteId);
        auditService.audit(
                ActionType.UPDATE_TRAINING, athlete.getLogin());
    }

    @Override
    public List<Training> getTrainingsSortedByDate(Long athleteId) {
        List<Training> allByAthleteId = trainingDao.findAllByAthleteId(athleteId);
        allByAthleteId.sort(Comparator.comparing(Training::getDate));

        Athlete athlete = athleteService.getAthleteByAthleteId(athleteId);
        auditService.audit(
                ActionType.GET_TRAININGS_SORTED_BY_DATE, athlete.getLogin());

        return allByAthleteId;
    }

    @Override
    public List<Training> getTrainingsSortedBySetsAmount(Long athleteId) {
        List<Training> allByAthleteId = trainingDao.findAllByAthleteId(athleteId);
        allByAthleteId.sort(Comparator.comparingInt(Training::getSetsAmount).reversed());

        Athlete athlete = athleteService.getAthleteByAthleteId(athleteId);
        auditService.audit(
                ActionType.GET_TRAININGS_SORTED_BY_SETS_AMOUNT, athlete.getLogin());

        return allByAthleteId;
    }

    @Override
    public void deleteTraining(Long athleteId, LocalDate date) {
        trainingDao.delete(getTrainingByAthleteIdAndDate(athleteId, date));

        Athlete athlete = athleteService.getAthleteByAthleteId(athleteId);
        auditService.audit(
                ActionType.GET_TRAININGS_SORTED_BY_SETS_AMOUNT, athlete.getLogin());
    }

    @Override
    public Training getTrainingByAthleteIdAndDate(Long athleteId, LocalDate date) {
        return trainingDao.findByAthleteIdAndTrainingDate(athleteId, date)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found!"));
    }
}