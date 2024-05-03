package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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

    public Contest getContestWithUser(Long id) {
        TypedQuery<Contest> query = entityManager.createQuery(
                "SELECT c FROM Contest c JOIN FETCH c.creator WHERE c.id = :id", Contest.class);
        query.setParameter("id", id);
        return query.getResultList().get(0);
    }

    // other CRUD operations
}