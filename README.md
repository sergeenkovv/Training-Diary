# Training-Diary

## A training diary application designed to record, view and analyze users' training data

## Content

- [Homework](#homework)
- [Tech stack](#tech-stack)
- [Functionality](#functionality)
- [Database structure](#database-structure)
- [API Endpoints](#api-endpoints)
- [Startup instructions](#startup-instructions)
- [Contact me](#contact-me)

## Homework

[HW №1](https://github.com/sergeenkovv/Training-Diary/pull/1)
[HW №2](https://github.com/sergeenkovv/Training-Diary/pull/2)
[HW №3](https://github.com/sergeenkovv/Training-Diary/pull/3)

## Tech stack

+ Java 17
+ PostgreSQL
+ Liquibase
+ Lombok
+ MapStruct
+ AspectJ
+ JWT
+ JUnit 5
+ AssertJ
+ Mockito
+ Testcontainers
+ Docker

## Functionality

- Athlete registration
- Athlete authorization
- Getting training types
- Adding training
- Editing training
- Get training history sorted by date or number of sets
- Deleting workout
- Getting an audit of athletes actions
- Getting execution times of methods
- Getting registered athletes (available to the trainer)
- Get training history sorted by date or number of approaches for any client (available to the trainer)
- Adding a training type (available to the trainer)
- Deleting a workout type (available to the trainer)

## Database structure

### `athletes`

| Column   | Type         | Comment                                               |
|----------|--------------|-------------------------------------------------------|
| id       | BIGINT       | The unique identifier of the athlete, the primary key |
| login    | VARCHAR(255) | Athlete login                                         |
| password | VARCHAR(255) | Athlete password                                      |
| role     | VARCHAR(255) | Athlete role                                          |

### `trainings`

| Column      | Type         | Comment                                                         |
|-------------|--------------|-----------------------------------------------------------------|
| id          | BIGINT       | The unique identifier for the training session, the primary key |
| sets_amount | VARCHAR(255) | The number of sets in the training session.                     |
| date        | TIMESTAMP    | The date of the training session                                |
| type_id     | BIGINT       | The type of training session                                    |
| athlete_id  | BIGINT       | The ID of the athlete associated with the training session      |

### `training_types`

| Column    | Type         | Comment                                                      |
|-----------|--------------|--------------------------------------------------------------|
| id        | BIGINT       | The unique identifier for the training type, the primary key |
| type_name | VARCHAR(255) | The name of the training type.                               |

## API Endpoints

**POST:** `/api/auth/registration` — Athlete registration.

```json
{
  "login": "vanya",
  "password": "1234"
}
```

___
**POST:** `/api/auth/authorization` — Athlete authorization.

```json
{
  "login": "vanya",
  "password": "1234"
}
```

___
**POST:** `/api/client/training/add` — Add training.

```json
{
  "athleteLogin": "vanya",
  "typeName": "BACK",
  "setsAmount": 3
}
```

___
**PUT:** `/api/client/training/edit` — Edit training.

```json
{
  "athleteLogin": "vanya",
  "typeName": "BACK",
  "setsAmount": 111,
  "date": "2024-04-29"
}
```

**DEL:** `/api/client/training/delete` — Delete training.

```json
{
  "athleteLogin": "vanya",
  "date": "2024-04-29"
}
```

___
**POST:** `/api/trainer/training-types/add` — Add training type.

```json
{
  "typeName": "12"
}
```

___
**DEL:** `/api/trainer/training-type/delete` — Delete training type.

```json
{
  "athleteLogin": "vanya",
  "date": "2024-04-29"
}
```

___
**GET:** `/api/client/training/show-by-date?login=vanya` — Get training by date.

**GET:** `/api/client/training/show-by-sets-amount?login=vanya` — Get training by sets amount.

___
**GET:** `/api/trainer/training/show-by-date?login=vanya` — Get training by date for any athlete.

**GET:** `/api/trainer/training/show-by-sets-amount?login=vanya` — Get training by sets amount for any athlete.

___
**GET:** `/api/client/training-types/show-training-types`  — Get all training athletes.

**GET:** `/api/trainer/training-types/show-training-types`  — Get all training athletes.

## Startup instructions

1. Start docker container with database. Run the command in the terminal in the root directory of the project: ` docker compose up `.
2. Build the application: ` mvn clean package `.
3. Run the application.

## Contact me

+ Email: [itproger181920@gmail.com](https://mail.google.com/mail/u/0/?view=cm&fs=1&tf=1&to=itproger181920@gmail.com) 📬
+ Telegram: [@itproger181920](https://t.me/itproger181920) ✈️
+ LinkedIn: [Ivan Sergeenkov](https://www.linkedin.com/in/ivan-sergeenkov-553419294?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app) 🌊