package com.ivan.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dto.ExceptionResponse;
import com.ivan.dto.TrainingHistoryResponse;
import com.ivan.exception.AthleteNotFoundException;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.ValidationParametersException;
import com.ivan.mapper.TrainingMapper;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.security.Authentication;
import com.ivan.service.AthleteService;
import com.ivan.service.TrainingService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.io.IOException;

@WebServlet(value = {"/api/client/training/show-by-date", "/api/trainer/training/show-by-date"})
public class ShowTrainingServletByDateServlet extends HttpServlet {

    @Setter
    private AthleteService athleteService;
    @Setter
    private TrainingService trainingService;
    @Setter
    private TrainingMapper trainingMapper;
    @Setter
    private ObjectMapper jacksonMapper;

    @Override
    public void init() {
        athleteService = (AthleteService) getServletContext().getAttribute("athleteService");
        trainingService = (TrainingService) getServletContext().getAttribute("trainingService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
        trainingMapper = (TrainingMapper) getServletContext().getAttribute("trainingMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.isAuth()) {
            try {
                viewTrainingByDate(req, resp, authentication);
            } catch (AthleteNotFoundException | ValidationParametersException e) {
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

    private void viewTrainingByDate(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws ValidationParametersException, IOException {
        String login = req.getParameter("login");
        if (login == null) throw new ValidationParametersException("Login parameter is null!");
        Athlete athlete = athleteService.getByLogin(login);
        if (authentication.getRole() != Role.TRAINER && !(authentication.getLogin().equals(athlete.getLogin()))) {
            throw new AuthorizationException("Incorrect credentials.");
        }
        TrainingHistoryResponse response = new TrainingHistoryResponse(athlete.getLogin(), trainingMapper.toDtoList(
                trainingService.getTrainingsSortedByDate(athlete.getId())));
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), response);
    }
}