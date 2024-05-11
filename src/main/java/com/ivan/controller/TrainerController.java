package com.ivan.controller;

import com.ivan.dto.*;
import com.ivan.mapper.AthleteMapper;
import com.ivan.mapper.TrainingMapper;
import com.ivan.mapper.TrainingTypeMapper;
import com.ivan.model.Athlete;
import com.ivan.security.SecurityUtils;
import com.ivan.service.AthleteService;
import com.ivan.service.TrainingService;
import com.ivan.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Add a training type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training type added successfully"),
            @ApiResponse(responseCode = "404", description = "Training Type already exist")
    })
    @PostMapping("/training-types/add")
    public ResponseEntity<?> addTrainingType(@RequestBody TrainingTypeRequest request) {
        trainingTypeService.addTrainingType(request.typeName());
        return ResponseEntity.ok(new SuccessResponse("Training type added successfully"));
    }

    @Operation(summary = "delete a training type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Training Type does not exist")
    })
    @DeleteMapping("/training-types/delete")
    public ResponseEntity<?> deleteTrainingType(@RequestBody TrainingTypeRequest request) {
        trainingTypeService.deleteTrainingType(request.typeName());
        return ResponseEntity.ok(new SuccessResponse("Training type deleted successfully"));
    }


    @Operation(summary = "show all athletes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show all athletes success"),
            @ApiResponse(responseCode = "404", description = "Exception!")
    })
    @GetMapping("client/show-all")
    public ResponseEntity<?> showAllAthletes() {
        List<AthleteResponse> athletes = athleteMapper.toDtoList(
                athleteService.getAllAthletes());
        return ResponseEntity.ok().body(new AthletesListResponse(athletes));
    }

    @Operation(summary = "Show all training type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show all training success"),
            @ApiResponse(responseCode = "404", description = "Exception!")
    })
    @GetMapping("/training-types/show-all")
    public ResponseEntity<?> showAllTrainingTypes() {
        List<TrainingTypeResponse> trainingTypes = trainingTypeMapper.toDtoList(
                trainingTypeService.getAllTrainingTypes());
        return ResponseEntity.ok().body(new TrainingTypesListResponse(trainingTypes));
    }

    @Operation(summary = "Show trainings by date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show Training by Date success"),
            @ApiResponse(responseCode = "404", description = "Exception!")
    })
    @GetMapping("/training/show-by-date")
    public ResponseEntity<?> showTrainingByDate(@RequestParam String login) {
        Athlete athlete = athleteService.getByLogin(login);
        List<TrainingResponse> trainings = trainingMapper.toDtoList(
                trainingService.getTrainingsSortedByDate(athlete.getId()));
        return ResponseEntity.ok().body(new TrainingHistoryResponse(login, trainings));
    }

    @Operation(summary = "Show trainings by sets amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show Training by sets amount"),
            @ApiResponse(responseCode = "404", description = "Exception!")
    })
    @GetMapping("/training/show-by-sets-amount")
    public ResponseEntity<?> showTrainingBySetsAmount(@RequestParam String login) {
        Athlete athlete = athleteService.getByLogin(login);
        List<TrainingResponse> trainings = trainingMapper.toDtoList(
                trainingService.getTrainingsSortedBySetsAmount(athlete.getId()));
        return ResponseEntity.ok().body(new TrainingHistoryResponse(login, trainings));
    }
}