package com.ivan;

import com.ivan.controller.AthleteController;
import com.ivan.controller.SecurityController;
import com.ivan.dao.AthleteDao;
import com.ivan.dao.AuditDao;
import com.ivan.dao.TrainingDao;
import com.ivan.dao.TrainingTypeDao;
import com.ivan.dao.impl.MemoryAthleteDaoImpl;
import com.ivan.dao.impl.MemoryAuditDaoImpl;
import com.ivan.dao.impl.MemoryTrainingDaoImpl;
import com.ivan.dao.impl.MemoryTrainingTypeDaoImpl;
import com.ivan.in.ConsoleInputData;
import com.ivan.in.ConsoleOutputData;
import com.ivan.model.Athlete;
import com.ivan.service.*;
import com.ivan.service.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple application context for managing beans and dependencies.
 * This class provides methods to load context, manage authentication, and retrieve beans.
 */
public class ApplicationContext {

    private static final Map<String, Object> CONTEXT = new HashMap<>();

    /**
     * Loads the application context with required beans.
     * This includes loading DAOs, services, controllers, and input/output components.
     */
    public static void loadContext() {
        loadDaoLayer();
        loadServiceLayer();
        loadControllers();
        loadInputOutputLayer();
    }

    /**
     * Loads the authorized athlete into the context.
     *
     * @param athlete the authenticated athlete
     */
    public static void loadAuthorizeAthlete(Athlete athlete) {
        CONTEXT.put("authorize", athlete);
    }

    /**
     * Cleans the authorized athlete from the context.
     */
    public static void cleanAuthorizeAthlete() {
        CONTEXT.remove("authorize");
    }

    /**
     * Retrieves the authorized athlete from the context.
     *
     * @return the authorized athlete
     */
    public static Athlete getAuthorizeAthlete() {
        return (Athlete) CONTEXT.get("authorize");
    }

    /**
     * Retrieves a bean by its name from the context.
     *
     * @param beanName the name of the bean to retrieve
     * @return the bean object
     */
    public static Object getBean(String beanName) {
        return CONTEXT.get(beanName);
    }

    /**
     * Loads the DAO layer implementations into the application context.
     * This includes instantiating and storing DAO objects for athlete, training, training type, and audit.
     */
    private static void loadDaoLayer() {
        CONTEXT.put("athleteDao", new MemoryAthleteDaoImpl());
        CONTEXT.put("trainingDao", new MemoryTrainingDaoImpl());
        CONTEXT.put("trainingTypeDao", new MemoryTrainingTypeDaoImpl());
        CONTEXT.put("auditDao", new MemoryAuditDaoImpl());
    }

    /**
     * Loads the service layer implementations into the application context.
     * This includes creating service objects with injected dependencies and storing them in the context.
     */
    private static void loadServiceLayer() {
        AuditService auditService = new AuditServiceImpl(
                (AuditDao) CONTEXT.get("auditDao")
        );
        CONTEXT.put("auditService", auditService);

        AthleteService athleteService = new AthleteServiceImpl(
                (AthleteDao) CONTEXT.get("athleteDao")
        );
        CONTEXT.put("athleteService", athleteService);

        SecurityService securityService = new SecurityServiceImpl(
                (AthleteDao) CONTEXT.get("athleteDao"),
                (AuditService) CONTEXT.get("auditService")
        );
        CONTEXT.put("securityService", securityService);

        TrainingTypeService trainingTypeService = new TrainingTypeServiceImpl(
                (TrainingTypeDao) CONTEXT.get("trainingTypeDao")
        );
        CONTEXT.put("trainingTypeService", trainingTypeService);

        TrainingService trainingService = new TrainingServiceImpl(
                (TrainingDao) CONTEXT.get("trainingDao"),
                (TrainingTypeService) CONTEXT.get("trainingTypeService"),
                (AuditService) CONTEXT.get("auditService"),
                (AthleteService) CONTEXT.get("athleteService")
        );
        CONTEXT.put("trainingService", trainingService);
    }

    /**
     * Loads the controller layer implementations into the application context.
     * This includes creating controller objects with injected services and storing them in the context.
     */
    private static void loadControllers() {
        AthleteController athleteController = new AthleteController(
                (AthleteService) CONTEXT.get("athleteService"),
                (AuditService) CONTEXT.get("auditService"),
                (TrainingService) CONTEXT.get("trainingService"),
                (TrainingTypeService) CONTEXT.get("trainingTypeService")
        );
        CONTEXT.put("athleteController", athleteController);

        SecurityController securityController = new SecurityController(
                (SecurityService) CONTEXT.get("securityService")
        );
        CONTEXT.put("securityController", securityController);
    }

    /**
     * Loads the input/output layer components into the application context.
     * This includes creating and storing input/output objects for console interaction.
     */
    private static void loadInputOutputLayer() {
        CONTEXT.put("input", new ConsoleInputData());
        CONTEXT.put("output", new ConsoleOutputData());
    }
}