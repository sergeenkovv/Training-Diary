package com.ivan.trainingdiary.service.impl;

import com.ivan.trainingdiary.exception.AthleteNotFoundException;
import com.ivan.trainingdiary.model.Athlete;
import com.ivan.trainingdiary.repository.AthleteRepository;
import com.ivan.trainingdiary.service.AthleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the {@link AthleteService} interface for managing athletes.
 *
 * @author sergeenkovv
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AthleteServiceImpl implements AthleteService {

    private final AthleteRepository athleteRepository;

    /**
     * Retrieves a list of all athletes.
     *
     * @return A list of all athletes.
     */
    @Override
    public List<Athlete> getAllAthletes() {
        return athleteRepository.findAll();
    }

    /**
     * Retrieves an athlete by their ID.
     *
     * @param id The ID of the athlete.
     * @return The athlete object.
     * @throws AthleteNotFoundException if no athlete is found with the given ID.
     */
    @Override
    public Athlete getById(Long id) {
        return athleteRepository.findById(id)
                .orElseThrow(() -> new AthleteNotFoundException("No athlete found with ID: " + id));
    }

    /**
     * Retrieves an athlete by their login.
     *
     * @param login The login of the athlete.
     * @return The athlete object.
     * @throws AthleteNotFoundException if no athlete is found with the given login.
     */
    @Override
    public Athlete getByLogin(String login) {
        return athleteRepository.findByLogin(login)
                .orElseThrow(() -> new AthleteNotFoundException("No athlete found with login: " + login));
    }
}