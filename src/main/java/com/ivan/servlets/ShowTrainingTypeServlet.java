package com.ivan.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dto.ExceptionResponse;
import com.ivan.dto.TrainingTypeResponse;
import com.ivan.dto.TrainingTypesListResponse;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.ValidationParametersException;
import com.ivan.mapper.TrainingTypeMapper;
import com.ivan.security.Authentication;
import com.ivan.service.TrainingTypeService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/api/client/training-types/show-training-types", "/api/trainer/training-types/show-training-types"})
public class ShowTrainingTypeServlet extends HttpServlet {

    private TrainingTypeService trainingTypeService;
    private TrainingTypeMapper trainingTypeMapper;
    private ObjectMapper jacksonMapper;

    @Override
    public void init() {
        trainingTypeService = (TrainingTypeService) getServletContext().getAttribute("trainingTypeService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
        trainingTypeMapper = (TrainingTypeMapper) getServletContext().getAttribute("trainingTypeMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.isAuth()) {
            try {
                viewTrainingType(resp, authentication);
            } catch (ValidationParametersException e) {
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

    private void viewTrainingType(HttpServletResponse resp, Authentication authentication) throws IOException {
        if (authentication.getLogin() == null) {
            throw new ValidationParametersException("Login parameter is null!");
        }
        List<TrainingTypeResponse> trainingTypeList = trainingTypeMapper.toDtoList(trainingTypeService.getAllTrainingTypes());
        TrainingTypesListResponse response = new TrainingTypesListResponse(trainingTypeList);
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), response);
    }
}