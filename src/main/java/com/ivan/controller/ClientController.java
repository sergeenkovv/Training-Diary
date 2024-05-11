package com.ivan.controller;

import com.ivan.dto.*;
import com.ivan.mapper.TrainingMapper;
import com.ivan.mapper.TrainingTypeMapper;
import com.ivan.model.Athlete;
import com.ivan.service.AthleteService;
import com.ivan.service.TrainingService;
import com.ivan.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class ClientController {

    private final AthleteService athleteService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final TrainingMapper trainingMapper;
    private final TrainingTypeMapper trainingTypeMapper;

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

    @Operation(summary = "Add a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training aded successfully"),
            @ApiResponse(responseCode = "404", description = "Training already exist")
    })
    @PostMapping("/training/add")
    public ResponseEntity<?> addTraining(@RequestBody TrainingRequest request) {
        Athlete athlete = athleteService.getByLogin(request.athleteLogin());
        trainingService.addTraining(athlete.getId(), request.typeName(), request.setsAmount());
        return ResponseEntity.ok(new SuccessResponse("Training added successfully"));
    }

    @Operation(summary = "delete a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Training does not exist")
    })
    @DeleteMapping("/training/delete")
    public ResponseEntity<?> deleteTraining(@RequestBody TrainingDateRequest request) {
        Athlete athlete = athleteService.getByLogin(request.athleteLogin());
        trainingService.deleteTraining(athlete.getId(), LocalDate.parse(request.date()));
        return ResponseEntity.ok(new SuccessResponse("Training deleted successfully"));
    }

    @Operation(summary = "edit a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training edited successfully"),
            @ApiResponse(responseCode = "404", description = "Training does not exist")
    })
    @PutMapping("/training/edit")
    public ResponseEntity<?> editTraining(@RequestBody TrainingEditRequest request) {
        Athlete athlete = athleteService.getByLogin(request.athleteLogin());
        trainingService.editTraining(athlete.getId(), LocalDate.parse(request.date()), request.typeName(), request.setsAmount());
        return ResponseEntity.ok(new SuccessResponse("Training edit successfully"));
    }
}