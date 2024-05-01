package com.ivan.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.dao.AthleteDao;
import com.ivan.dao.TrainingDao;
import com.ivan.dao.TrainingTypeDao;
import com.ivan.dao.impl.AthleteDaoImpl;
import com.ivan.dao.impl.TrainingDaoImpl;
import com.ivan.dao.impl.TrainingTypeDaoImpl;
import com.ivan.liquibase.LiquibaseMigration;
import com.ivan.mapper.AthleteMapper;
import com.ivan.mapper.TrainingMapper;
import com.ivan.mapper.TrainingTypeMapper;
import com.ivan.security.JwtTokenProvider;
import com.ivan.service.AthleteService;
import com.ivan.service.SecurityService;
import com.ivan.service.TrainingService;
import com.ivan.service.TrainingTypeService;
import com.ivan.service.impl.AthleteServiceImpl;
import com.ivan.service.impl.SecurityServiceImpl;
import com.ivan.service.impl.TrainingServiceImpl;
import com.ivan.service.impl.TrainingTypeServiceImpl;
import com.ivan.util.ConnectionManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Context listener that initializes the application context, including loading properties, database configuration,
 * and service context initialization.
 *
 * @author sergeenkovv
 */
@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private Properties properties;
    private ConnectionManager connectionProvider;

    /**
     * Initializes the application context when the servlet context is initialized.
     *
     * @param sce the servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        loadProperties(servletContext);
        databaseConfiguration(servletContext);
        serviceContextInit(servletContext);

        ObjectMapper jacksonMapper = new ObjectMapper();
        servletContext.setAttribute("jacksonMapper", jacksonMapper);
        servletContext.setAttribute("athleteMapper", AthleteMapper.INSTANCE);
        servletContext.setAttribute("trainingMapper", TrainingMapper.INSTANCE);
        servletContext.setAttribute("trainingTypeMapper", TrainingTypeMapper.INSTANCE);
    }

    /**
     * Destroys the application context when the servlet context is destroyed.
     *
     * @param sce the servlet context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    /**
     * Loads the application properties from the specified file path.
     *
     * @param servletContext the servlet context
     */
    private void loadProperties(ServletContext servletContext) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(servletContext.getResourceAsStream("/WEB-INF/classes/application.properties"));
                servletContext.setAttribute("servletProperties", properties);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Property file not found!");
            } catch (IOException e) {
                throw new RuntimeException("Error reading configuration file: " + e.getMessage());
            }
        }
    }

    /**
     * Configures the database connection and Liquibase migrations.
     *
     * @param servletContext the servlet context
     */
    private void databaseConfiguration(ServletContext servletContext) {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String driver = properties.getProperty("db.driver");

        connectionProvider = new ConnectionManager(url, username, password, driver);
        servletContext.setAttribute("connectionProvider", connectionProvider);

        String changeLogFile = properties.getProperty("liquibase.changeLogFile");
        String schemaName = properties.getProperty("liquibase.schemaName");

        if (Boolean.parseBoolean(properties.getProperty("liquibase.enabled"))) {
            LiquibaseMigration liquibaseMigration = new LiquibaseMigration(connectionProvider.getConnection(), changeLogFile, schemaName);
            liquibaseMigration.runMigrations();
            servletContext.setAttribute("liquibaseMigration", liquibaseMigration);
        }
    }

    /**
     * Initializes the service context, including DAO, service, and utility classes.
     *
     * @param servletContext the servlet context
     */
    private void serviceContextInit(ServletContext servletContext) {
        AthleteDao athleteDao = new AthleteDaoImpl(connectionProvider);
        TrainingTypeDao trainingTypeDao = new TrainingTypeDaoImpl(connectionProvider);
        TrainingDao trainingDao = new TrainingDaoImpl(connectionProvider, trainingTypeDao, athleteDao);

        AthleteService athleteService = new AthleteServiceImpl(athleteDao);

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
                properties.getProperty("jwt.secret"),
                Long.parseLong(properties.getProperty("jwt.access")),
                athleteService
        );

        SecurityService securityService = new SecurityServiceImpl(athleteDao, jwtTokenProvider);
        TrainingTypeService trainingTypeService = new TrainingTypeServiceImpl(trainingTypeDao);
        TrainingService trainingService = new TrainingServiceImpl(trainingDao, trainingTypeService, athleteService);

        servletContext.setAttribute("athleteService", athleteService);
        servletContext.setAttribute("jwtTokenProvider", jwtTokenProvider);
        servletContext.setAttribute("securityService", securityService);
        servletContext.setAttribute("trainingTypeService", trainingTypeService);
        servletContext.setAttribute("trainingService", trainingService);
    }
}