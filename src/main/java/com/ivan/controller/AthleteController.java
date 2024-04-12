package com.ivan.controller;

import com.ivan.exception.NotValidArgumentException;
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

    public void addTraining(Long athleteId, String trainingType, String setsAmount, LocalDate date) {
        if (!isValidNum(setsAmount)) {
            throw new NotValidArgumentException("Enter a number greater than 0. letters cannot be used!");
        }

        if (!isValidDate(date)) {
            throw new NotValidArgumentException("Incorrect date format. it should match yyyy-MM-dd!");
        }

        trainingService.addTraining(athleteId, trainingType, Integer.parseInt(setsAmount), date);
    }

    public void editTrainingByAthleteIdAndTrainingDate(Long athleteId, LocalDate date, String trainingType, String setsAmount) {
        if (!isValidNum(setsAmount)) {
            throw new NotValidArgumentException("Enter a number greater than 0. letters cannot be used!");
        }

        if (!isValidDate(date)) {
            throw new NotValidArgumentException("Incorrect date format. it should match yyyy-MM-dd!");
        }

        trainingService.updateTraining(athleteId, date, trainingType, setsAmount);
    }

    public List<Training> showHistoryTrainingsSortedByDate(Long athleteId) {
        return trainingService.getTrainingsSortedByDate(athleteId);
    }

    public List<Training> showHistoryTrainingsBySetsAmount(Long athleteId) {
        return trainingService.getTrainingsSortedBySetsAmount(athleteId);
    }

    public void deleteTraining(Long athleteId, LocalDate date) {
        if (!isValidDate(date)) {
            throw new NotValidArgumentException("Incorrect date format. it should match yyyy-MM-dd!");
        }

        trainingService.deleteTraining(athleteId, date);
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

    public List<Audit> showAuditsByAthleteId(String athlete) {
        return auditService.getAllAuditsByAthleteId(athlete);
    }

    private boolean isValidNum(String... inputs) {
        for (String input : inputs) {
            if (!input.matches("\\d+")) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedDate = date.format(formatter);

        return formattedDate.equals(date.toString());
    }
}