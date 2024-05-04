package com.ivan.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dto.SuccessResponse;
import com.ivan.dto.TrainingRequest;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.model.Training;
import com.ivan.model.TrainingType;
import com.ivan.security.Authentication;
import com.ivan.service.AthleteService;
import com.ivan.service.TrainingService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddTrainingServletTest {

    @Mock
    private TrainingService trainingService;
    @Mock
    private AthleteService athleteService;
    @Mock
    private ObjectMapper jacksonMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletInputStream inputStream;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private ServletContext servletContext;

    @InjectMocks
    private AddTrainingServlet addTrainingServlet;

    Athlete athlete;
    Athlete trainer;
    Training training;
    TrainingType trainingType;
    TrainingRequest trainingRequest;
    Authentication authentication;
    SuccessResponse successResponse;

    @BeforeEach
    void setup() {
        addTrainingServlet = new AddTrainingServlet() {
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        addTrainingServlet.setAthleteService(athleteService);
        addTrainingServlet.setTrainingService(trainingService);
        addTrainingServlet.setJacksonMapper(jacksonMapper);

        athlete = Athlete.builder()
                .id(1L)
                .login("Ivan")
                .password("1234")
                .role(Role.CLIENT)
                .build();

        trainer = Athlete.builder()
                .id(2L)
                .login("trainer")
                .password("trainer")
                .role(Role.TRAINER)
                .build();

        trainingType = TrainingType.builder()
                .id(1L)
                .typeName("LEGS")
                .build();

        training = Training.builder()
                .id(1L)
                .setsAmount(3)
                .date(LocalDate.now())
                .trainingType(trainingType)
                .athlete(athlete)
                .build();

        trainingRequest = new TrainingRequest(athlete.getLogin(), training.getTrainingType().getTypeName(), training.getSetsAmount());

        authentication = new Authentication(athlete.getLogin(), athlete.getRole(), true, null);

        successResponse = new SuccessResponse("Training added successfully");
    }

    @Test
    @DisplayName("doPost success")
    void doPost_Success() throws IOException {
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);
        when(request.getInputStream()).thenReturn(inputStream);
        when(jacksonMapper.readValue(inputStream, TrainingRequest.class)).thenReturn(trainingRequest);
        when(athleteService.getByLogin(trainingRequest.athleteLogin())).thenReturn(athlete);
        when(response.getWriter()).thenReturn(printWriter);

        trainingService.addTraining(athlete.getId(), trainingRequest.athleteLogin(), trainingRequest.setsAmount());

        addTrainingServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(jacksonMapper).writeValue(printWriter, successResponse);
    }
}