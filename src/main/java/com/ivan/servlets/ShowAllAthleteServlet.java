package com.ivan.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dto.*;
import com.ivan.exception.AthleteNotFoundException;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.ValidationParametersException;
import com.ivan.mapper.AthleteMapper;
import com.ivan.model.Role;
import com.ivan.security.Authentication;
import com.ivan.service.AthleteService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/trainer/training-types/show-all-athletes")
public class ShowAllAthleteServlet extends HttpServlet {

    private AthleteService athleteService;
    private AthleteMapper athleteMapper;
    private ObjectMapper jacksonMapper;

    @Override
    public void init() {
        athleteService = (AthleteService) getServletContext().getAttribute("athleteService");
        athleteMapper = (AthleteMapper) getServletContext().getAttribute("athleteMapper");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.isAuth()) {
            try {
                showAllAthletes(resp, authentication);
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

    private void showAllAthletes(HttpServletResponse resp, Authentication authentication) throws IOException {
        if (!authentication.getRole().equals(Role.TRAINER)) {
            throw new AuthorizationException("Incorrect credentials! You are not a trainer!");
        }

        if (authentication.getLogin() == null) {
            throw new ValidationParametersException("Login parameter is null!");
        }

        List<AthleteResponse> athleteHistory = athleteMapper.toDtoList(athleteService.getAllAthletes());
        AthletesListResponse response = new AthletesListResponse(athleteHistory);
        resp.setStatus(HttpServletResponse.SC_OK);
        jacksonMapper.writeValue(resp.getWriter(), response);
    }
}