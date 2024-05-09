package com.ivan.controller;

import com.ivan.dto.JwtResponse;
import com.ivan.dto.SecurityRequest;
import com.ivan.mapper.AthleteMapper;
import com.ivan.model.Athlete;
import com.ivan.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SecurityController {

    private final SecurityService securityService;
    private final AthleteMapper athleteMapper;

    @Operation(summary = "registration athlete in application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Athlete authorized successfully"),
            @ApiResponse(responseCode = "404", description = "Invalid request body")
    })
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody SecurityRequest request) {
        Athlete athlete = securityService.registration(request.login(), request.password());
        return ResponseEntity.ok(athleteMapper.toDto(athlete));
    }

    @Operation(summary = "Authorize athlete in application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Athlete authorized successfully"),
            @ApiResponse(responseCode = "404", description = "Invalid request body")
    })
    @PostMapping("/authorization")
    public ResponseEntity<JwtResponse> authorize(@RequestBody SecurityRequest request) {
        JwtResponse response = securityService.authorization(request.login(), request.password());
        return ResponseEntity.ok(response);
    }
}