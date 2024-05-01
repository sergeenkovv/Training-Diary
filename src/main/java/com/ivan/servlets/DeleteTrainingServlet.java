package com.ivan.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dto.ExceptionResponse;
import com.ivan.dto.SuccessResponse;
import com.ivan.dto.TrainingDateRequest;
import com.ivan.exception.*;
import com.ivan.model.Athlete;
import com.ivan.security.Authentication;
import com.ivan.service.AthleteService;
import com.ivan.service.TrainingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/api/client/training/delete")
public class DeleteTrainingServlet extends HttpServlet {

    private AthleteService athleteService;
    private TrainingService trainingService;
    private ObjectMapper jacksonMapper;

    @Override
    public void init() {
        athleteService = (AthleteService) getServletContext().getAttribute("athleteService");
        trainingService = (TrainingService) getServletContext().getAttribute("trainingService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.isAuth()) {
            try (ServletInputStream inputStream = req.getInputStream()) {
                TrainingDateRequest request = jacksonMapper.readValue(inputStream, TrainingDateRequest.class);

                Athlete athlete = athleteService.getByLogin(request.athleteLogin());
                if (!authentication.getLogin().equals(athlete.getLogin())) {
                    throw new AuthorizationException("Incorrect credentials!");
                }

                trainingService.deleteTraining(athlete.getId(), LocalDate.parse(request.date()));

                resp.setStatus(HttpServletResponse.SC_OK);
                jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Training deleted successfully"));
            } catch (AthleteNotFoundException | TrainingLimitExceededException | InvalidTrainingTypeException |
                     InvalidArgumentException | ValidationParametersException | TrainingNotFoundException e) {
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