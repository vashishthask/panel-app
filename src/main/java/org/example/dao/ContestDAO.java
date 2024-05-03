package org.example.dao;

import jakarta.persistence.EntityManager;
import org.example.model.Contest;

public class ContestDAO {

    private final EntityManager entityManager;

    public ContestDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Contest createContest(Contest contest) {
        entityManager.persist(contest);
        return contest;
    }

    public Contest getContest(Long id) {
        return entityManager.find(Contest.class, id);
    }

    // other CRUD operations
}