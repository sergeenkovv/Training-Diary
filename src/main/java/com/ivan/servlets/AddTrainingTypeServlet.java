package com.ivan.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dto.ExceptionResponse;
import com.ivan.dto.SuccessResponse;
import com.ivan.dto.TrainingTypeRequest;
import com.ivan.exception.*;
import com.ivan.model.Role;
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

@WebServlet("/api/trainer/training-types/add")
public class AddTrainingTypeServlet extends HttpServlet {

    private TrainingTypeService trainingTypeService;
    private ObjectMapper jacksonMapper;

    @Override
    public void init() {
        trainingTypeService = (TrainingTypeService) getServletContext().getAttribute("trainingTypeService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.isAuth()) {
            try (ServletInputStream inputStream = req.getInputStream()) {
                if (authentication.getRole().equals(Role.TRAINER)) {
                    TrainingTypeRequest request = jacksonMapper.readValue(inputStream, TrainingTypeRequest.class);

                    trainingTypeService.addTrainingType(request.typeName());

                    resp.setStatus(HttpServletResponse.SC_OK);
                    jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Training added successfully"));
                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(authentication.getMessage()));
                }
            } catch (AthleteNotFoundException | InvalidTrainingTypeException | InvalidArgumentException |
                     DuplicateTrainingTypeException e) {
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