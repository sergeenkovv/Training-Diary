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

/**
 * Service implementation for managing athlete trainings.
 * This class provides methods to add, edit, and retrieve athlete trainings.
 * It interacts with the database through {@link TrainingDao} for data access,
 * {@link TrainingTypeService} to retrieve training types,
 * {@link AuditService} for auditing purposes, and
 * {@link AthleteService} to retrieve athlete information.
 */
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDao;
    private final TrainingTypeService trainingTypeService;
    private final AuditService auditService;
    private final AthleteService athleteService;

    /**
     * Adds a training for the specified athlete.
     * Throws {@link TrainingLimitExceededException} if the athlete has already performed
     * a training of the same type on the current day.
     *
     * @param athleteId    the ID of the athlete
     * @param trainingType the type of training to add
     * @param setsAmount   the number of sets for the training
     */
    @Override
    public void addTraining(Long athleteId, Long trainingType, Integer setsAmount) {
        Optional<Training> maybeTraining = trainingDao.findByAthleteIdAndTrainingDate(athleteId, LocalDate.now());
        TrainingType byTypeId = trainingTypeService.getByTypeId(trainingType);

        if (maybeTraining.isPresent()) {
            throw new TrainingLimitExceededException("You cannot do one type of training more than once a day!");
        }

        Training training = Training.builder()
                .setsAmount(setsAmount)
                .date(LocalDate.now())
                .typeId(byTypeId.getId())
                .athleteId(athleteId)
                .build();

        Athlete athlete = athleteService.getAthleteByAthleteId(athleteId);
        auditService.audit(
                ADD_TRAINING, athlete.getLogin());

        trainingDao.save(training);
    }

    /**
     * Edits a training for the specified athlete.
     *
     * @param athleteId    the ID of the athlete
     * @param date         the date of the training to edit
     * @param trainingType the new type of training
     * @param setsAmount   the new number of sets
     */
    @Override
    public void editTraining(Long athleteId, LocalDate date, Long trainingType, String setsAmount) {
        Training existingTraining = getTrainingByAthleteIdAndDate(athleteId, date);
        TrainingType byTypeId = trainingTypeService.getByTypeId(trainingType);

        existingTraining.setTypeId(byTypeId.getId());
        existingTraining.setSetsAmount(Integer.parseInt(setsAmount));

        trainingDao.update(existingTraining);

        Athlete athlete = athleteService.getAthleteByAthleteId(athleteId);
        auditService.audit(
                ActionType.UPDATE_TRAINING, athlete.getLogin());
    }

    /**
     * Retrieves a list of athlete trainings sorted by date.
     *
     * @param athleteId the ID of the athlete
     * @return a list of athlete trainings sorted by date
     */
    @Override
    public List<Training> getTrainingsSortedByDate(Long athleteId) {
        List<Training> allByAthleteId = trainingDao.findAllByAthleteId(athleteId);
        allByAthleteId.sort(Comparator.comparing(Training::getDate));

        Athlete athlete = athleteService.getAthleteByAthleteId(athleteId);
        auditService.audit(
                ActionType.GET_TRAININGS_SORTED_BY_DATE, athlete.getLogin());

        return allByAthleteId;
    }

    /**
     * Retrieves a list of athlete trainings sorted by sets amount in descending order.
     *
     * @param athleteId the ID of the athlete
     * @return a list of athlete trainings sorted by sets amount
     */
    @Override
    public List<Training> getTrainingsSortedBySetsAmount(Long athleteId) {
        List<Training> allByAthleteId = trainingDao.findAllByAthleteId(athleteId);
        allByAthleteId.sort(Comparator.comparingInt(Training::getSetsAmount).reversed());

        Athlete athlete = athleteService.getAthleteByAthleteId(athleteId);
        auditService.audit(
                ActionType.GET_TRAININGS_SORTED_BY_SETS_AMOUNT, athlete.getLogin());

        return allByAthleteId;
    }

    /**
     * Deletes a training for the specified athlete on the given date.
     *
     * @param athleteId the ID of the athlete
     * @param date      the date of the training to delete
     */
    @Override
    public void deleteTraining(Long athleteId, LocalDate date) {
        Training training = getTrainingByAthleteIdAndDate(athleteId, date);
        trainingDao.delete(training.getId());

        Athlete athlete = athleteService.getAthleteByAthleteId(athleteId);
        auditService.audit(
                ActionType.GET_TRAININGS_SORTED_BY_SETS_AMOUNT, athlete.getLogin());
    }

    /**
     * Retrieves a training for the specified athlete on the given date.
     *
     * @param athleteId the ID of the athlete
     * @param date      the date of the training
     * @return the training object
     * @throws TrainingNotFoundException if the training is not found
     */
    @Override
    public Training getTrainingByAthleteIdAndDate(Long athleteId, LocalDate date) {
        return trainingDao.findByAthleteIdAndTrainingDate(athleteId, date)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found!"));
    }
}