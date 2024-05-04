package org.example.dao;

import jakarta.persistence.EntityManager;
import org.example.model.ReviewCycle;

import java.util.Optional;

public class ReviewCycleDao {
    private final EntityManager em;

    public ReviewCycleDao(EntityManager entityManager) {
        em = entityManager;
    }

    public ReviewCycle save(ReviewCycle reviewCycle){
        em.persist(reviewCycle);
        return reviewCycle;
    }

    public Optional<ReviewCycle> findById(Long id) {
        return Optional.ofNullable(em.find(ReviewCycle.class, id));
    }
}
