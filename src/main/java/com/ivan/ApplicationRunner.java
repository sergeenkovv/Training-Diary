package com.ivan;

import com.ivan.controller.AthleteController;
import com.ivan.controller.SecurityController;
import com.ivan.exception.*;
import com.ivan.in.InputData;
import com.ivan.in.OutputData;
import com.ivan.model.*;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.ivan.ApplicationRunner.HandlerSecurity.isTrainer;

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
                    case TRAINER_MENU -> trainerProcess(inputData, outputData);
                    case CLIENT_MENU -> clientProcess(inputData, outputData);
                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (AthleteNotFoundException |
                     AuthorizationException |
                     InvalidTrainingTypeException |
                     InvalidArgumentException |
                     RegistrationException |
                     TrainingLimitExceededException |
                     DateTimeParseException |
                     TrainingNotFoundException e) {
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

    private static void clientProcess(InputData inputData, OutputData outputData) {
        final String menu = """
                ╔═════════════════════════════════════════════════╗
                  1. View types of training.
                  2. Add a new training.
                  3. Edit training
                  4. Get previous workouts sorted by date.
                  5. Show statistics — Get trainings by sets amount.
                  6. Delete training.
                  7. Log out of your account.
                  0. Quit the application.
                ╚═════════════════════════════════════════════════╝
                  """;
        while (true) {
            outputData.output(menu);
            String input = inputData.input().toString();
            if (input.equals("1")) {
                HandlerClient.showAvailableTrainingsTypes(outputData);
            } else if (input.equals("2")) {
                HandlerClient.handlerAddTraining(inputData, outputData);
            } else if (input.equals("3")) {
                HandlerClient.handlerEditTraining(inputData, outputData);
            } else if (input.equals("4")) {
                HandlerClient.handlerGetPreviousTrainingsSortedByDate(outputData);
            } else if (input.equals("5")) {
                HandlerClient.HandlerGetTrainingsBySetsAmount(outputData);
            } else if (input.equals("6")) {
                HandlerClient.HandlerDoDeleteTraining(inputData, outputData);
            } else if (input.equals("7")) {
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

    private static void trainerProcess(InputData inputData, OutputData outputData) {
        final String menu = """
                 ╔═════════════════════════════════════════════════╗
                 1. Get a list of registered clients.
                 2. View types of training.
                 3. Add a new training type.
                 4. Delete training type.
                 5. Get the athlete's trainings sorted by date.
                 6. Get the athlete's trainings sorted by sets amount.
                 7. Get athlete audit.
                 8. Log out of your account.
                 0. Quit the application.
                 ╚═════════════════════════════════════════════════╝
                """;
        while (true) {
            outputData.output(menu);
            String input = inputData.input().toString();
            if (input.equals("1")) {
                HandlerTrainer.HandlerShowAllClients(outputData);
            } else if (input.equals("2")) {
                HandlerTrainer.showAvailableTrainingsTypes(outputData);
            } else if (input.equals("3")) {
                HandlerTrainer.HandlerAddNewTypeOfTraining(inputData, outputData);
            } else if (input.equals("4")) {
                HandlerTrainer.HandlerDoDeleteTrainingType(inputData, outputData);
            } else if (input.equals("5")) {
                HandlerTrainer.HandlerGetClientsPreviousTrainingsSortedByDate(inputData, outputData);
            } else if (input.equals("6")) {
                HandlerTrainer.HandlerGetClientsPreviousTrainingsSortedBySetsAmount(inputData, outputData);
            } else if (input.equals("7")) {
                HandlerTrainer.HandlerGetAthleteAudits(inputData, outputData);
            } else if (input.equals("8")) {
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

        TRAINER_MENU,
        /**
         * TrainingDiaryApplication menu process stage.
         */
        CLIENT_MENU,
        /**
         * Exit process stage.
         */
        EXIT
    }

    public static class HandlerSecurity {
        private static void handlerAuthorize(InputData inputData, OutputData outputData) {
            final String loginMsg = "Enter login:";
            outputData.output(loginMsg);
            String login = inputData.input().toString();

            final String passwordMsg = "Enter password:";
            outputData.output(passwordMsg);
            String password = inputData.input().toString();

            Athlete authorizedAthlete = securityController.authorize(login, password);
            if (authorizedAthlete != null) {
                ApplicationContext.loadAuthorizeAthlete(authorizedAthlete);
                if (isTrainer(authorizedAthlete)) {
                    currentStage = ProcessStage.TRAINER_MENU;
                    return;
                }
                currentStage = ProcessStage.CLIENT_MENU;
                return;
            }
            currentStage = ProcessStage.CLIENT_MENU;
        }

        private static void handlerRegister(InputData inputData, OutputData outputData) {
            final String loginMsg = "Enter login:";
            outputData.output(loginMsg);
            String login = inputData.input().toString();

            final String passwordMsg = "Enter password. The password cannot be empty and must be between 3 and 32 characters long:";
            outputData.output(passwordMsg);
            String password = inputData.input().toString();

            Athlete registeredAthlete = securityController.register(login, password);
            ApplicationContext.loadAuthorizeAthlete(registeredAthlete);
            currentStage = ProcessStage.CLIENT_MENU;
        }

        public static boolean isTrainer(Athlete athlete) {
            return athlete.getRole().equals(Role.TRAINER);
        }
    }

    private static class HandlerTrainer {

        public static void HandlerShowAllClients(OutputData outputData) {
            final String msgCl = "List of all clients:";
            outputData.output(msgCl);

            List<Athlete> clientsList = athleteController.showAllClients();
            List<Athlete> trainersList = new ArrayList<>();

            for (Athlete ath : clientsList) {
                if (isTrainer(ath)) trainersList.add(ath);
                else {
                    outputData.output(ath);
                }
            }

            final String msgTr = "List of all trainers:";
            outputData.output(msgTr);
            for (Athlete trainer : trainersList) {
                outputData.output(trainer);
            }
        }

        public static void HandlerAddNewTypeOfTraining(InputData inputData, OutputData outputData) {
            final String trainingTypeMsg = "Enter training type:";
            outputData.output(trainingTypeMsg);
            String trainingType = inputData.input().toString();

            athleteController.addTrainingType(
                    TrainingType.builder()
                            .typeName(trainingType)
                            .build());
        }

        public static void showAvailableTrainingsTypes(OutputData outputData) {
            HandlerClient.showAvailableTrainingsTypes(outputData);
        }

        public static void HandlerDoDeleteTrainingType(InputData inputData, OutputData outputData) {
            final String trainingTypeMsg = "Enter the type of training you want to delete:";
            outputData.output(trainingTypeMsg);
            String trainingType = inputData.input().toString();

            athleteController.deleteTrainingType(trainingType);
        }

        public static void HandlerGetClientsPreviousTrainingsSortedByDate(InputData inputData, OutputData outputData) {
            final String athleteMsg = "Enter the athlete id whose workouts you want to see";
            outputData.output(athleteMsg);
            String athlete = inputData.input().toString();

            List<Training> trainingList = athleteController.showHistoryTrainingsSortedByDate(Long.parseLong(athlete));
            if (trainingList == null || trainingList.isEmpty()) {
                outputData.output("You have not a training history.\n");
            } else {
                outputData.output(trainingList);
            }
        }

        public static void HandlerGetClientsPreviousTrainingsSortedBySetsAmount(InputData inputData, OutputData outputData) {
            final String athleteMsg = "Enter the athlete id whose workouts you want to see";
            outputData.output(athleteMsg);
            String athlete = inputData.input().toString();

            List<Training> trainingList = athleteController.showHistoryTrainingsBySetsAmount(Long.parseLong(athlete));
            if (trainingList == null || trainingList.isEmpty()) {
                outputData.output("You have not a training history.\n");
            } else {
                outputData.output(trainingList);
            }
        }

        public static void HandlerGetAthleteAudits(InputData inputData, OutputData outputData) {
            final String athleteMsg = "Enter the athlete name whose audits you want to see";
            outputData.output(athleteMsg);
            String athlete = inputData.input().toString();

            List<Audit> auditList = athleteController.showAuditsByAthleteLogin(athlete);

            if (auditList == null || auditList.isEmpty()) {
                outputData.output("You have not a training history.\n");
            } else {
                outputData.output(auditList);
            }
        }
    }

    private static class HandlerClient {

        public static void showAvailableTrainingsTypes(OutputData outputData) {
            List<TrainingType> allTypes = athleteController.showAvailableTrainingTypes();
            outputData.output(allTypes);
        }

        public static void handlerAddTraining(InputData inputData, OutputData outputData) {
            List<TrainingType> allTypes = athleteController.showAvailableTrainingTypes();
            outputData.output(allTypes);

            final String trainingTypeMsg = "select training type";
            outputData.output(trainingTypeMsg);
            String trainingType = inputData.input().toString();

            final String setsAmountMsg = "select sets amount";
            outputData.output(setsAmountMsg);
            String setsAmount = inputData.input().toString();

            athleteController.addTraining(ApplicationContext.getAuthorizeAthlete().getId(), trainingType, setsAmount);
        }

        public static void handlerEditTraining(InputData inputData, OutputData outputData) {
            final String dateMsg = "Enter the date of study you want to change in the format yyyy-MM-dd";
            outputData.output(dateMsg);
            String date = inputData.input().toString();

            final String typeTrainingMsg = "Enter new type training:";
            outputData.output(typeTrainingMsg);
            String typeTraining = inputData.input().toString();

            final String setsAmountMsg = "Enter new sets amount:";
            outputData.output(setsAmountMsg);
            String setsAmount = inputData.input().toString();

            athleteController.editTrainingByAthleteIdAndTrainingDate(ApplicationContext.getAuthorizeAthlete().getId(), date, typeTraining, setsAmount);
        }

        public static void handlerGetPreviousTrainingsSortedByDate(OutputData outputData) {
            List<Training> trainingList = athleteController.showHistoryTrainingsSortedByDate(ApplicationContext.getAuthorizeAthlete().getId());
            if (trainingList == null || trainingList.isEmpty()) {
                outputData.output("You have not a training history.\n");
            } else {
                outputData.output(trainingList);
            }
        }

        public static void HandlerGetTrainingsBySetsAmount(OutputData outputData) {
            List<Training> trainingList = athleteController.showHistoryTrainingsBySetsAmount(ApplicationContext.getAuthorizeAthlete().getId());
            if (trainingList == null || trainingList.isEmpty()) {
                outputData.output("You have not a training history.\n");
            } else {
                outputData.output(trainingList);
            }
        }

        public static void HandlerDoDeleteTraining(InputData inputData, OutputData outputData) {
            final String dateMsg = "Enter the training date you want to delete in the format yyyy-MM-dd:";
            outputData.output(dateMsg);
            String date = inputData.input().toString();

            athleteController.deleteTraining(ApplicationContext.getAuthorizeAthlete().getId(), date);
        }
    }
}