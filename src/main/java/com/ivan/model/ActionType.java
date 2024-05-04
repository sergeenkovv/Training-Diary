package com.ivan.model;

/**
 * Represents different types of actions.
 * These actions include registration, authorization, adding training, updating training,
 * retrieving trainings sorted by date or sets amount, and deleting training.
 *
 * @author sergeenkovv
 */
public enum ActionType {

    REGISTRATION,
    AUTHORIZATION,
    ADD_TRAINING,
    EDIT_TRAINING,
    DELETE_TRAINING,
    GET_TRAININGS_SORTED_BY_DATE,
    GET_TRAININGS_SORTED_BY_SETS_AMOUNT
}