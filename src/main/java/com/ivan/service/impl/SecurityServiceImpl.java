package com.ivan.service.impl;

import com.ivan.annotations.Auditable;
import com.ivan.annotations.Loggable;
import com.ivan.dao.AthleteDao;
import com.ivan.dto.JwtResponse;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.RegistrationException;
import com.ivan.model.Athlete;
import com.ivan.model.Role;
import com.ivan.security.JwtTokenProvider;
import com.ivan.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the {@link SecurityService} interface providing
 * functionality for athlete registration and authorization.
 * <p>
 * Requires an {@link AthleteDao} to be
 * injected for data access.
 *
 * @author sergeenkovv
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityServiceImpl implements SecurityService {

    private final AthleteDao athleteDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new athlete with the provided login and password.
     *
     * @param login    The login for the new athlete.
     * @param password The password for the new athlete.
     * @return The registered athlete.
     * @throws RegistrationException If an athlete with the same login already exists.
     */
    @Override
    @Loggable
    @Auditable
    @Transactional
    public Athlete registration(String login, String password) {
        Optional<Athlete> athlete = athleteDao.findByLogin(login);

        if (athlete.isPresent()) {
            throw new RegistrationException("The athlete with this login already exists!");
        }

        Athlete newAthlete = Athlete.builder()
                .login(login)
                .password(passwordEncoder.encode(password))
                .role(Role.CLIENT)
                .build();

        return athleteDao.save(newAthlete);
    }

    /**
     * Authorizes an athlete with the provided login and password.
     *
     * @param login    The login of the athlete.
     * @param password The password of the athlete.
     * @return The authorized athlete.
     * @throws AuthorizationException If no athlete with the provided login is found
     *                                or if the provided password is incorrect.
     */
    @Override
    @Loggable
    @Auditable
    @Transactional
    public JwtResponse authorization(String login, String password) {
        Optional<Athlete> optionalPlayer = athleteDao.findByLogin(login);

        if (optionalPlayer.isEmpty()) {
            throw new AuthorizationException("There is no player with this login in the database!");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

        String accessToken = jwtTokenProvider.createAccessToken(login);
        return new JwtResponse(login, accessToken);
    }
}