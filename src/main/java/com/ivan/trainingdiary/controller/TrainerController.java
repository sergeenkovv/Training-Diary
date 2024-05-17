package com.ivan.trainingdiary.controller;

import com.ivan.trainingdiary.dto.*;
import com.ivan.trainingdiary.mapper.AthleteMapper;
import com.ivan.trainingdiary.mapper.TrainingMapper;
import com.ivan.trainingdiary.mapper.TrainingTypeMapper;
import com.ivan.trainingdiary.model.Athlete;
import com.ivan.trainingdiary.security.SecurityUtils;
import com.ivan.trainingdiary.service.AthleteService;
import com.ivan.trainingdiary.service.TrainingService;
import com.ivan.trainingdiary.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API controller for trainer
 *
 * @author sergeenkovv
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainer")
public class TrainerController {

    private final AthleteService athleteService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final AthleteMapper athleteMapper;
    private final TrainingMapper trainingMapper;
    private final TrainingTypeMapper trainingTypeMapper;

    /**
     * Adds a new training type.
     *
     * @param request the request body containing the training type details
     * @return the response entity with the status of the operation
     */
    @Operation(summary = "Add a training type")
    @PostMapping("/training-types/add")
    public ResponseEntity<?> addTrainingType(@RequestBody TrainingTypeRequest request) {
        if (!SecurityUtils.isValidLogin(request.athleteLogin())) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login!"));
        trainingTypeService.addTrainingType(request.typeName());
        return ResponseEntity.ok(new SuccessResponse("Training type added successfully"));
    }

    /**
     * Deletes a training type.
     *
     * @param request the request body containing the training type details
     * @return the response entity with the status of the operation
     */
    @Operation(summary = "delete a training type")
    @DeleteMapping("/training-types/delete")
    public ResponseEntity<?> deleteTrainingType(@RequestBody TrainingTypeRequest request) {
        if (!SecurityUtils.isValidLogin(request.athleteLogin())) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login!"));
        trainingTypeService.deleteTrainingType(request.typeName());
        return ResponseEntity.ok(new SuccessResponse("Training type deleted successfully"));
    }

    /**
     * Shows all athletes.
     *
     * @param login the login of the trainer
     * @return the response entity with the list of athletes
     */
    @Operation(summary = "show all athletes")
    @GetMapping("athlete/show-all")
    public ResponseEntity<?> showAllAthletes(@RequestParam String login) {
        if (!SecurityUtils.isValidLogin(login)) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login!"));
        List<AthleteResponse> athletes = athleteMapper.toDtoList(
                athleteService.getAllAthletes());
        return ResponseEntity.ok().body(new AthletesListResponse(athletes));
    }

    /**
     * Shows all training types.
     *
     * @param login the login of the trainer
     * @return the response entity with the list of training types
     */
    @Operation(summary = "Show all training type")
    @GetMapping("/training-types/show-all")
    public ResponseEntity<?> showAllTrainingTypes(@RequestParam String login) {
        if (!SecurityUtils.isValidLogin(login)) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login!"));
        List<TrainingTypeResponse> trainingTypes = trainingTypeMapper.toDtoList(
                trainingTypeService.getAllTrainingTypes());
        return ResponseEntity.ok().body(new TrainingTypesListResponse(trainingTypes));
    }

    /**
     * Shows trainings by date.
     *
     * @param login    the login of the trainer
     * @param loginClient the login of the client
     * @return the response entity with the list of trainings sorted by date
     */
    @Operation(summary = "Show trainings by date")
    @GetMapping("/training/show-by-date")
    public ResponseEntity<?> showTrainingByDate(@RequestParam String login, @RequestParam String loginClient) {
        if (!SecurityUtils.isValidLogin(login)) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login!"));
        Athlete athlete = athleteService.getByLogin(loginClient);
        List<TrainingResponse> trainings = trainingMapper.toDtoList(
                trainingService.getTrainingsSortedByDate(athlete.getId()));
        return ResponseEntity.ok().body(new TrainingHistoryResponse(loginClient, trainings));
    }

    /**
     * Shows trainings by sets amount.
     *
     * @param login    the login of the trainer
     * @param loginClient the login of the client
     * @return the response entity with the list of trainings sorted by sets amount
     */
    @Operation(summary = "Show trainings by sets amount")
    @GetMapping("/training/show-by-sets-amount")
    public ResponseEntity<?> showTrainingBySetsAmount(@RequestParam String login, @RequestParam String loginClient) {
        if (!SecurityUtils.isValidLogin(login)) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login!"));
        Athlete athlete = athleteService.getByLogin(loginClient);
        List<TrainingResponse> trainings = trainingMapper.toDtoList(
                trainingService.getTrainingsSortedBySetsAmount(athlete.getId()));
        return ResponseEntity.ok().body(new TrainingHistoryResponse(loginClient, trainings));
    }
}