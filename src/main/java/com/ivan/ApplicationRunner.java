package com.ivan;

import com.ivan.controller.AthleteController;
import com.ivan.controller.SecurityController;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.NotValidArgumentException;
import com.ivan.exception.RegistrationException;
import com.ivan.exception.TrainingLimitExceededException;
import com.ivan.in.InputData;
import com.ivan.in.OutputData;
import com.ivan.model.Athlete;
import com.ivan.model.TrainingType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ApplicationRunner {

    private static AthleteController athleteController;
    private static SecurityController securityController;

    private static ProcessStage currentStage;

    public static void run() {
        ApplicationContext.loadContext();
        InputData inputData = (InputData) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        athleteController = (AthleteController) ApplicationContext.getBean("athleteController");
        securityController = (SecurityController) ApplicationContext.getBean("securityController");
        currentStage = ProcessStage.SECURITY;
        outputData.output("Welcome!\n");

        boolean processIsRun = true;
        while (processIsRun) {
            try {
                switch (currentStage) {
                    case SECURITY -> securityProcess(inputData, outputData);
                    case MAIN_MENU -> menuProcess(inputData, outputData);
                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (AuthorizationException |
                     NotValidArgumentException |
                     RegistrationException |
                     TrainingLimitExceededException e) {
                log.warn(e.getMessage());
                outputData.errOutput(e.getMessage());
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                outputData.errOutput("Unknown error. More details " + e.getMessage());
                processIsRun = false;
            }
        }
        inputData.closeInput();
    }

    private static void securityProcess(InputData inputData, OutputData outputData) {
        final String menu = """
                ╔═══════════════════════════════════════════════════════╗
                  Please register or log in to the application.
                  Enter one number without spaces or other characters:
                  1. Registration.
                  2. Login.
                  3. End the program.
                ╚═══════════════════════════════════════════════════════╝
                  """;

        while (true) {
            outputData.output(menu);
            Object input = inputData.input();
            if (input.toString().equals("1")) {
                HandlerSecurity.handlerRegister(inputData, outputData);
                break;
            } else if (input.toString().equals("2")) {
                HandlerSecurity.handlerAuthorize(inputData, outputData);
                break;
            } else if (input.toString().equals("3")) {
                currentStage = ProcessStage.EXIT;
                break;
            } else {
                outputData.output("Unknown command, try again.");
            }
        }
    }

    private static void menuProcess(InputData inputData, OutputData outputData) {
        final String menu = """
                ╔═════════════════════════════════════════════════╗
                  1. View types of training.
                  2. Add a new training.
                  3. Edit training
                  4. Get previous workouts.
                  5. Log out of your account.
                  0. Quit the application.
                ╚═════════════════════════════════════════════════╝
                  """;// TODO: 10.04.2024 тренер будет трени добавлять. пользователь лишь смотрит доступные трени
        while (true) {
            outputData.output(menu);
            String input = inputData.input().toString();
            if (input.equals("1")) {
                HandlerMenu.showAvailableTrainingsTypes(outputData);
            } else if (input.equals("2")) {
                HandlerMenu.handlerAddTraining(outputData, inputData);
            } else if (input.equals("3")) {
                HandlerMenu.handlerEditTraining(inputData, outputData);
            } else if (input.equals("4")) {
                HandlerMenu.handlerGetPreviousTrainings(outputData);
            } else if (input.equals("5")) {
                ApplicationContext.cleanAuthorizeAthlete();
                currentStage = ProcessStage.SECURITY;
                break;
            } else if (input.equals("0")) {
                ApplicationContext.cleanAuthorizeAthlete();
                currentStage = ProcessStage.EXIT;
            } else {
                outputData.output("Unknown command, try again.\n");
            }
        }
    }

    private static void exitProcess(OutputData outputData) {
        final String message = "Goodbye!";
        outputData.output(message);
        ApplicationContext.cleanAuthorizeAthlete();
    }

    private enum ProcessStage {
        /**
         * Security process stage.
         */
        SECURITY,
        /**
         * MonitoringServiceApplication menu process stage.
         */
        MAIN_MENU,
        /**
         * Exit process stage.
         */
        EXIT
    }

    private static class HandlerMenu {

        public static void showAvailableTrainingsTypes(OutputData outputData) {
            List<TrainingType> allTypes = athleteController.showAvailableTrainingTypes();
            outputData.output(allTypes);
        }

        public static void handlerAddTraining(OutputData outputData, InputData inputData) {
            List<TrainingType> allTypes = athleteController.showAvailableTrainingTypes();
            outputData.output(allTypes);

            final String readingType = "select training type";
            outputData.output(readingType);

            String typeOutp = inputData.input().toString();

            final String readingSets = "select sets amount";
            outputData.output(readingSets);

            String setsOutp = inputData.input().toString();

            athleteController.addTraining(typeOutp, setsOutp);
        }

        public static void handlerEditTraining(InputData inputData, OutputData outputData) {

        }

        public static void handlerGetPreviousTrainings(OutputData outputData) {

        }
    }

    public static class HandlerSecurity {
        private static void handlerAuthorize(InputData inputData, OutputData outputData) {
            final String loginMsg = "Enter login:";
            outputData.output(loginMsg);
            String login = inputData.input().toString();
            final String passMsg = "Enter password:";
            outputData.output(passMsg);
            String password = inputData.input().toString();

            Athlete authorizedAthlete = securityController.authorize(login, password);
            ApplicationContext.loadAuthorizeAthlete(authorizedAthlete);
            currentStage = ProcessStage.MAIN_MENU;
        }

        private static void handlerRegister(InputData inputData, OutputData outputData) {
            final String loginMsg = "Enter login:";
            outputData.output(loginMsg);
            String login = inputData.input().toString();
            final String passMsg = "Enter password. The password cannot be empty and must be between 3 and 32 characters long:";
            outputData.output(passMsg);
            String password = inputData.input().toString();

            Athlete registeredAthlete = securityController.register(login, password);
            ApplicationContext.loadAuthorizeAthlete(registeredAthlete);
            currentStage = ProcessStage.MAIN_MENU;
        }
    }
}