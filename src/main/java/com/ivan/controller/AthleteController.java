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

@Slf4j
@RequiredArgsConstructor
public class AthleteController {

    private final AthleteService athleteService;
    private final AuditService auditService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    public List<TrainingType> showAvailableTrainingTypes() {
        return trainingTypeService.getAvailableTrainingTypes();
    }

    public void addTraining(Long athleteId, String trainingType, String setsAmount) {
        if (!isValidNum(setsAmount)) {
            throw new InvalidArgumentException("Enter a number greater than 0. letters cannot be used!");
        }

        trainingService.addTraining(athleteId, trainingType, Integer.parseInt(setsAmount));
    }

    public void editTrainingByAthleteIdAndTrainingDate(Long athleteId, String date, String trainingType, String setsAmount) {
        if (!isValidNum(setsAmount)) {
            throw new InvalidArgumentException("Enter a number greater than 0. letters cannot be used!");
        }

        if (!isValidDate(LocalDate.parse(date))) {
            throw new DateTimeParseException("Incorrect date format. it should match yyyy-MM-dd!", date, -1);
        }

        trainingService.updateTraining(athleteId, LocalDate.parse(date), trainingType, setsAmount);
    }

    public List<Training> showHistoryTrainingsSortedByDate(Long athleteId) {
        return trainingService.getTrainingsSortedByDate(athleteId);
    }

    public List<Training> showHistoryTrainingsBySetsAmount(Long athleteId) {
        return trainingService.getTrainingsSortedBySetsAmount(athleteId);
    }

    public void deleteTraining(Long athleteId, String date) {
        if (!isValidDate(LocalDate.parse(date))) {
            throw new InvalidArgumentException("Incorrect date format. it should match yyyy-MM-dd!");
        }
        trainingService.deleteTraining(athleteId, LocalDate.parse(date));
    }

    public List<Athlete> showAllClients() {
        return athleteService.showAllAthlete();
    }

    public void addTrainingType(TrainingType trainingType) {
        trainingTypeService.save(trainingType);
    }

    public void deleteTrainingType(String trainingTypeName) {
        trainingTypeService.delete(trainingTypeName);
    }

    public List<Audit> showAuditsByAthleteLogin(String login) {
        return auditService.getAllAuditsByAthleteLogin(login);
    }

    private boolean isValidNum(String... inputs) {
        return Arrays.stream(inputs)
                .allMatch(input -> input.matches("[1-9]\\d*"));
    }

    private boolean isValidDate(LocalDate date) {
        String dateFormat = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String formattedDate = formatter.format(date);
        return formattedDate.equals(date.toString());
    }
}