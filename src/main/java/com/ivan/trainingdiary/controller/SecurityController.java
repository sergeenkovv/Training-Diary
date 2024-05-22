package com.ivan.trainingdiary.controller;

import com.ivan.trainingdiary.dto.JwtResponse;
import com.ivan.trainingdiary.dto.SecurityRequest;
import com.ivan.trainingdiary.mapper.AthleteMapper;
import com.ivan.trainingdiary.model.Athlete;
import com.ivan.trainingdiary.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SecurityController class that handles security-related API endpoints for authentication and authorization.
 *
 * @author sergeenkovv
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SecurityController {

    private final SecurityService securityService;
    private final AthleteMapper athleteMapper;

    /**
     * Handles the registration process for a new athletes.
     *
     * @param request The SecurityRequest object containing registration details.
     * @return ResponseEntity<?> containing the registered player information.
     */
    @Operation(summary = "registration athlete in application")
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody @Valid SecurityRequest request) {
        Athlete athlete = securityService.registration(request.login(), request.password());
        return ResponseEntity.ok(athleteMapper.toDto(athlete));
    }

    /**
     * Handles the authorization process for an existing athletes.
     *
     * @param request The SecurityRequest object containing authorization details.
     * @return ResponseEntity<?> containing the JWT response for successful authorization.
     */
    @Operation(summary = "Authorize athlete in application")
    @PostMapping("/authorization")
    public ResponseEntity<JwtResponse> authorize(@RequestBody @Valid SecurityRequest request) {
        JwtResponse response = securityService.authorization(request.login(), request.password());
        return ResponseEntity.ok(response);
    }
}