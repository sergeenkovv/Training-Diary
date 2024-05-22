package com.ivan.trainingdiary.controller;

import com.ivan.trainingdiary.dto.*;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * API controller for client.
 *
 * @author sergeenkovv
 */
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

    /**
     * Show all training type.
     *
     * @param login of athlete
     * @return {@link ResponseEntity} with list of {@link TrainingTypeResponse}
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
     * Show trainings by date.
     *
     * @param login of athlete
     * @return {@link ResponseEntity} with list of {@link TrainingResponse}
     */
    @Operation(summary = "Show trainings by date")
    @GetMapping("/training/show-by-date")
    public ResponseEntity<?> showTrainingByDate(@RequestParam String login) {
        if (!SecurityUtils.isValidLogin(login)) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login!"));
        Athlete athlete = athleteService.getByLogin(login);
        List<TrainingResponse> trainings = trainingMapper.toDtoList(
                trainingService.getTrainingsSortedByDate(athlete.getId()));
        return ResponseEntity.ok().body(new TrainingHistoryResponse(login, trainings));
    }

    /**
     * Show trainings by sets amount.
     *
     * @param login of athlete
     * @return {@link ResponseEntity} with list of {@link TrainingResponse}
     */
    @Operation(summary = "Show trainings by sets amount")
    @GetMapping("/training/show-by-sets-amount")
    public ResponseEntity<?> showTrainingBySetsAmount(@RequestParam String login) {
        if (!SecurityUtils.isValidLogin(login)) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login!"));
        Athlete athlete = athleteService.getByLogin(login);
        List<TrainingResponse> trainings = trainingMapper.toDtoList(
                trainingService.getTrainingsSortedBySetsAmount(athlete.getId()));
        return ResponseEntity.ok().body(new TrainingHistoryResponse(login, trainings));
    }

    /**
     * Add a training.
     *
     * @param request {@link TrainingRequest}
     * @return {@link ResponseEntity} with {@link SuccessResponse}
     */
    @Operation(summary = "Add a training")
    @PostMapping("/training/add")
    public ResponseEntity<?> addTraining(@RequestBody TrainingRequest request) {
        if (!SecurityUtils.isValidLogin(request.athleteLogin())) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Athlete athlete = athleteService.getByLogin(request.athleteLogin());
        trainingService.addTraining(athlete.getId(), request.typeName(), request.setsAmount());
        return ResponseEntity.ok(new SuccessResponse("Training added successfully"));
    }

    /**
     * Delete a training.
     *
     * @param request {@link TrainingDateRequest}
     * @return {@link ResponseEntity} with {@link SuccessResponse}
     */
    @Operation(summary = "delete a training")
    @DeleteMapping("/training/delete")
    public ResponseEntity<?> deleteTraining(@RequestBody TrainingDateRequest request) {
        if (!SecurityUtils.isValidLogin(request.athleteLogin())) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Athlete athlete = athleteService.getByLogin(request.athleteLogin());
        trainingService.deleteTraining(athlete.getId(), LocalDate.parse(request.date()));
        return ResponseEntity.ok(new SuccessResponse("Training deleted successfully"));
    }

    /**
     * Edit a training.
     *
     * @param request {@link TrainingEditRequest}
     * @return {@link ResponseEntity} with {@link SuccessResponse}
     */
    @Operation(summary = "edit a training")
    @PutMapping("/training/edit")
    public ResponseEntity<?> editTraining(@RequestBody TrainingEditRequest request) {
        if (!SecurityUtils.isValidLogin(request.athleteLogin())) return ResponseEntity.badRequest()
                .body(new ExceptionResponse("Incorrect login"));
        Athlete athlete = athleteService.getByLogin(request.athleteLogin());
        trainingService.editTraining(athlete.getId(), LocalDate.parse(request.date()), request.typeName(), request.setsAmount());
        return ResponseEntity.ok(new SuccessResponse("Training edit successfully"));
    }
}