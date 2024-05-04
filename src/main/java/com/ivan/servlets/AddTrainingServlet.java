package com.ivan.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dto.ExceptionResponse;
import com.ivan.dto.SuccessResponse;
import com.ivan.dto.TrainingRequest;
import com.ivan.exception.*;
import com.ivan.mapper.TrainingMapper;
import com.ivan.model.Athlete;
import com.ivan.security.Authentication;
import com.ivan.service.AthleteService;
import com.ivan.service.TrainingService;
import com.ivan.service.TrainingTypeService;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.io.IOException;

@WebServlet("/api/client/training/add")
public class AddTrainingServlet extends HttpServlet {

    @Setter
    private AthleteService athleteService;
    @Setter
    private TrainingService trainingService;
    @Setter
    private ObjectMapper jacksonMapper;

    @Override
    public void init() {
        athleteService = (AthleteService) getServletContext().getAttribute("athleteService");
        trainingService = (TrainingService) getServletContext().getAttribute("trainingService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.isAuth()) {
            try (ServletInputStream inputStream = req.getInputStream()) {
                TrainingRequest request = jacksonMapper.readValue(inputStream, TrainingRequest.class);

                Athlete athlete = athleteService.getByLogin(request.athleteLogin());
                if (!authentication.getLogin().equals(athlete.getLogin())) {
                    throw new AuthorizationException("Incorrect credentials!");
                }

                trainingService.addTraining(athlete.getId(), request.typeName(), request.setsAmount());

                resp.setStatus(HttpServletResponse.SC_OK);
                jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Training added successfully"));
            } catch (AthleteNotFoundException | TrainingLimitExceededException | InvalidTrainingTypeException |
                     InvalidArgumentException | ValidationParametersException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (AuthorizationException e) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(authentication.getMessage()));
        }
    }
}