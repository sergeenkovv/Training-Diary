package com.ivan.dao;

import java.util.List;

public interface GeneralDao<K, E> { //k - key, e - entity

    List<E> findAll();
    E save(E entity);
}