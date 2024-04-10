package com.ivan.dao;

import com.ivan.model.Training;

import java.util.List;

public interface TrainingDao extends GeneralDao<Long, Training> {

    List<Training> findAllByOrderByDateAsc();
}
