package com.ivan.dao;

public interface GeneralDao<K, E> { //k - key, e - entity

    E save(E entity);
}