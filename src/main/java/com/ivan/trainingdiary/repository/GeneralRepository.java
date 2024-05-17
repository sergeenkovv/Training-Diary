package com.ivan.trainingdiary.repository;

/**
 * Interface for a generic data access object (DAO).
 *
 * @param <K> The type of the key used to identify entities.
 * @param <E> The type of the entity managed by the DAO.
 * @author sergeenkovv
 */
public interface GeneralRepository<K, E> {

    /**
     * Saves an entity to the data source.
     *
     * @param entity The entity to save.
     * @return The saved entity.
     */
    E save(E entity);
}