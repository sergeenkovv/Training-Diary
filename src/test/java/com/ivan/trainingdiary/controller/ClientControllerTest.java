package com.ivan.trainingdiary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.trainingdiary.dto.*;
import com.ivan.trainingdiary.mapper.AthleteMapper;
import com.ivan.trainingdiary.mapper.TrainingMapper;
import com.ivan.trainingdiary.mapper.TrainingTypeMapper;
import com.ivan.trainingdiary.model.Athlete;
import com.ivan.trainingdiary.model.Role;
import com.ivan.trainingdiary.model.Training;
import com.ivan.trainingdiary.model.TrainingType;
import com.ivan.trainingdiary.security.JwtTokenProvider;
import com.ivan.trainingdiary.service.AthleteService;
import com.ivan.trainingdiary.service.TrainingService;
import com.ivan.trainingdiary.service.TrainingTypeService;
import com.ivan.trainingdiary.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ClientController.class)
@WithMockUser(username = "vanya", password = "123", roles = {"CLIENT", "TRAINER"})
class ClientControllerTest {

    @MockBean
    private AthleteService athleteService;
    @MockBean
    private TrainingService trainingService;
    @MockBean
    private TrainingTypeService trainingTypeService;
    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @MockBean
    private AthleteMapper athleteMapper;
    @MockBean
    private TrainingMapper trainingMapper;
    @MockBean
    private TrainingTypeMapper trainingTypeMapper;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @Autowired
    private MockMvc mockMvc;

    private Athlete athlete;
    private TrainingType trainingType1;
    private TrainingType trainingType2;
    private TrainingTypeResponse trainingTypeResponse1;
    private TrainingTypeResponse trainingTypeResponse2;
    private TrainingTypesListResponse trainingTypesListResponse;
    private Training training1;
    private Training training2;
    private TrainingRequest trainingRequest1;
    private TrainingRequest trainingRequest2;
    private TrainingDateRequest trainingDateRequest;
    private TrainingEditRequest trainingEditRequest;
    private TrainingResponse trainingResponse1;
    private TrainingResponse trainingResponse2;
    private TrainingHistoryResponse trainingHistoryResponse;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        athlete = Athlete.builder()
                .id(1L)
                .login("vanya")
                .password(passwordEncoder.encode("123"))
                .role(Role.CLIENT)
                .build();

        trainingType1 = TrainingType.builder()
                .id(1L)
                .typeName("CARDIO")
                .build();

        trainingType2 = TrainingType.builder()
                .id(2L)
                .typeName("SWIMMING")
                .build();

        trainingTypeResponse1 = new TrainingTypeResponse(trainingType1.getTypeName());
        trainingTypeResponse2 = new TrainingTypeResponse(trainingType2.getTypeName());

        trainingTypesListResponse = new TrainingTypesListResponse(Arrays.asList(trainingTypeResponse1, trainingTypeResponse2));

        training1 = Training.builder()
                .id(1L)
                .setsAmount(3)
                .date(LocalDate.parse("2022-11-11"))
                .trainingType(trainingType1)
                .athlete(athlete)
                .build();

        training2 = Training.builder()
                .id(2L)
                .setsAmount(3)
                .date(LocalDate.parse("2022-12-09"))
                .trainingType(trainingType2)
                .athlete(athlete)
                .build();

        trainingRequest1 = new TrainingRequest(athlete.getLogin(), training1.getTrainingType().getTypeName(), training1.getSetsAmount());
        trainingDateRequest = new TrainingDateRequest(athlete.getLogin(), String.valueOf(training1.getDate()));
        trainingEditRequest = new TrainingEditRequest(athlete.getLogin(), training1.getTrainingType().getTypeName(), training1.getSetsAmount(), String.valueOf(training1.getDate()));

        trainingResponse1 = new TrainingResponse(training1.getId(), training1.getSetsAmount(), training1.getTrainingType().getTypeName(), String.valueOf(training1.getDate()));
        trainingResponse2 = new TrainingResponse(training2.getId(), training2.getSetsAmount(), training2.getTrainingType().getTypeName(), String.valueOf(training2.getDate()));
        trainingHistoryResponse = new TrainingHistoryResponse(athlete.getLogin(), Arrays.asList(trainingResponse1, trainingResponse2));

        objectMapper = new ObjectMapper();
    }

    @DisplayName("Test showAllTrainingTypes method")
    @Test
    void showAllTrainingTypes_Success() throws Exception {
        given(athleteService.getByLogin(athlete.getLogin())).willReturn(athlete);
        given(trainingTypeMapper.toDtoList(trainingTypeService.getAllTrainingTypes())).willReturn(trainingTypesListResponse.types());

        ResultActions perform = mockMvc.perform(get("/api/client/training-types/show-all")
                .with(csrf())
                .param("login", athlete.getLogin())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(trainingTypesListResponse)));
    }

    @DisplayName("Test showTrainingByDate method")
    @Test
    void showTrainingByDate_Success() throws Exception {
        given(athleteService.getByLogin(athlete.getLogin())).willReturn(athlete);
        given(trainingMapper.toDtoList(trainingService.getTrainingsSortedByDate(athlete.getId()))).willReturn(trainingHistoryResponse.trainings());

        ResultActions perform = mockMvc.perform(get("/api/client/training/show-by-date")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .param("login", athlete.getLogin())
                .accept(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(trainingHistoryResponse)));
    }

    @DisplayName("Test showTrainingBySetsAmount method")
    @Test
    void showTrainingBySetsAmount_Success() throws Exception {
        given(athleteService.getByLogin(athlete.getLogin())).willReturn(athlete);
        given(trainingMapper.toDtoList(trainingService.getTrainingsSortedBySetsAmount(athlete.getId()))).willReturn(trainingHistoryResponse.trainings());

        ResultActions perform = mockMvc.perform(get("/api/client/training/show-by-date")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .param("login", athlete.getLogin())
                .accept(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(trainingHistoryResponse)));
    }

    @DisplayName("Test addTraining method")
    @Test
    void addTraining_Success() throws Exception {
        given(athleteService.getByLogin(athlete.getLogin())).willReturn(athlete);
        doNothing().when(trainingService).addTraining(athlete.getId(), trainingRequest1.typeName(), trainingResponse1.setsAmount());

        ResultActions perform = mockMvc.perform(post("/api/client/training/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainingRequest1))
                .accept(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Training added successfully"));
    }

    @DisplayName("Test deleteTraining method")
    @Test
    void deleteTraining_Success() throws Exception {
        given(athleteService.getByLogin(athlete.getLogin())).willReturn(athlete);
        doNothing().when(trainingService).deleteTraining(athlete.getId(), LocalDate.parse(trainingDateRequest.date()));

        ResultActions perform = mockMvc.perform(delete("/api/client/training/delete")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainingDateRequest))
                .accept(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Training deleted successfully"));
    }

    @DisplayName("Test editTraining method")
    @Test
    void editTraining_Success() throws Exception {
        given(athleteService.getByLogin(athlete.getLogin())).willReturn(athlete);
        doNothing().when(trainingService).editTraining(athlete.getId(), LocalDate.parse(trainingEditRequest.date()), trainingEditRequest.typeName(), trainingEditRequest.setsAmount());

        ResultActions perform = mockMvc.perform(delete("/api/client/training/delete")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainingDateRequest))
                .accept(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Training deleted successfully"));
    }
}