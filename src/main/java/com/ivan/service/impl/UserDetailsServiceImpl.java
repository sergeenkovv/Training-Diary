package com.ivan.service.impl;

import com.ivan.dao.AthleteDao;
import com.ivan.model.Athlete;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Service implementation for retrieving user details.
 * This service provides user details for authentication and authorization.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AthleteDao athleteDao;

    /**
     * Load user details by username for authentication.
     *
     * @param username The username of the user
     * @return UserDetails object containing user details
     * @throws UsernameNotFoundException if the user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Athlete athlete = athleteDao.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username + "!"));
        return new User(athlete.getLogin(), athlete.getPassword(), Collections.singleton(athlete.getRole()));
    }
}