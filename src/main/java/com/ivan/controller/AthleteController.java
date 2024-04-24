package com.ivan.controller;

import com.ivan.exception.InvalidArgumentException;
import com.ivan.model.Athlete;
import com.ivan.model.Audit;
import com.ivan.model.Training;
import com.ivan.model.TrainingType;
import com.ivan.service.AthleteService;
import com.ivan.service.AuditService;
import com.ivan.service.TrainingService;
import com.ivan.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Controller class responsible for handling athlete-related operations and interactions.
 * This class provides methods to manage athlete training activities, including adding, editing,
 * and deleting training sessions, as well as displaying available training types and training history.
 * It interacts with the {@link AthleteService} for athlete-related operations,
 * {@link AuditService} for auditing purposes,
 * {@link TrainingService} for training management, and
 * {@link TrainingTypeService} for accessing available training types.
 *
 * @author sergeenkovv
 */
@Slf4j
@RequiredArgsConstructor
public class AthleteController {

    private final AthleteService athleteService;
    private final AuditService auditService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    /**
     * Retrieves a list of available training types.
     *
     * @return a list of available training types
     */
    public List<TrainingType> showAvailableTrainingTypes() {
        return trainingTypeService.getAllTrainingTypes();
    }

    /**
     * Adds a training session for the specified athlete.
     *
     * @param athleteId    the ID of the athlete
     * @param trainingType the type of training to add
     * @param setsAmount   the number of sets for the training
     * @throws InvalidArgumentException if the sets amount provided is not a valid number
     */
    public void addTraining(Long athleteId, String trainingType, String setsAmount) {
        if (!isValidNum(setsAmount)) {
            throw new InvalidArgumentException("Enter a number greater than 0. letters cannot be used!");
        }

        trainingService.addTraining(athleteId, trainingType, Integer.parseInt(setsAmount));
    }

    /**
     * Edits a training session for the specified athlete and date.
     *
     * @param athleteId    the ID of the athlete
     * @param date         the date of the training to edit
     * @param trainingType the new type of training
     * @param setsAmount   the new number of sets
     * @throws InvalidArgumentException if the sets amount provided is not a valid number,
     *                                  or if the date provided is not in the correct format
     */
    public void editTrainingByAthleteIdAndTrainingDate(Long athleteId, String date, String trainingType, String setsAmount) {
        if (!isValidNum(setsAmount)) {
            throw new InvalidArgumentException("Enter a number greater than 0. letters cannot be used!");
        }

        if (!isValidDate(LocalDate.parse(date))) {
            throw new DateTimeParseException("Incorrect date format. it should match yyyy-MM-dd!", date, -1);
        }

        trainingService.editTraining(athleteId, LocalDate.parse(date), trainingType, Integer.parseInt(setsAmount));
    }

    /**
     * Retrieves the training history of the specified athlete, sorted by date.
     *
     * @param athleteId the ID of the athlete
     * @return a list of training sessions sorted by date
     */
    public List<Training> showHistoryTrainingsSortedByDate(Long athleteId) {
        return trainingService.getTrainingsSortedByDate(athleteId);
    }

    /**
     * Retrieves the training history of the specified athlete, sorted by sets amount.
     *
     * @param athleteId the ID of the athlete
     * @return a list of training sessions sorted by sets amount
     */
    public List<Training> showHistoryTrainingsBySetsAmount(Long athleteId) {
        return trainingService.getTrainingsSortedBySetsAmount(athleteId);
    }

    /**
     * Deletes a training session for the specified athlete and date.
     *
     * @param athleteId the ID of the athlete
     * @param date      the date of the training to delete
     * @throws InvalidArgumentException if the date provided is not in the correct format
     */
    public void deleteTraining(Long athleteId, String date) {
        if (!isValidDate(LocalDate.parse(date))) {
            throw new InvalidArgumentException("Incorrect date format. it should match yyyy-MM-dd!");
        }
        trainingService.deleteTraining(athleteId, LocalDate.parse(date));
    }

    /**
     * Retrieves a list of all clients (athletes).
     *
     * @return a list of all clients (athletes)
     */
    public List<Athlete> showAllClients() {
        return athleteService.showAllAthletes();
    }

    /**
     * Adds a new training type.
     *
     * @param trainingType the training type to add
     */
    public void addTrainingType(TrainingType trainingType) {
        trainingTypeService.addTrainingType(trainingType);
    }

    /**
     * Deletes a training type by its name.
     *
     * @param trainingTypeName the name of the training type to delete
     */
    public void deleteTrainingType(String trainingType) {
        trainingTypeService.deleteTrainingType(trainingType);
    }

    /**
     * Retrieves a list of audits for a specific athlete login.
     *
     * @param login the login of the athlete
     * @return a list of audits for the specified athlete
     */
    public List<Audit> showAuditsByAthleteLogin(String login) {
        return auditService.getAllAuditsByAthleteLogin(login);
    }

    /**
     * Validates if the provided inputs are valid numbers.
     *
     * @param inputs the inputs to validate
     * @return true if all inputs are valid numbers, false otherwise
     */
    private boolean isValidNum(String... inputs) {
        return Arrays.stream(inputs)
                .allMatch(input -> input.matches("[1-9]\\d*"));
    }

    /**
     * Validates if the provided date is in the correct format.
     *
     * @param date the date to validate
     * @return true if the date is in the correct format, false otherwise
     */
    private boolean isValidDate(LocalDate date) {
        String dateFormat = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String formattedDate = formatter.format(date);
        return formattedDate.equals(date.toString());
    }
}