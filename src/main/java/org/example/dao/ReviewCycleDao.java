package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.ReviewCycle;

import java.util.List;
import java.util.Optional;

public class ReviewCycleDao {
    private final EntityManager em;

    public ReviewCycleDao(EntityManager entityManager) {
        em = entityManager;
    }

    public ReviewCycle save(ReviewCycle reviewCycle) {
        em.persist(reviewCycle);
        return reviewCycle;
    }

    public Optional<ReviewCycle> findById(Long id) {
        return Optional.ofNullable(em.find(ReviewCycle.class, id));
    }

    public ReviewCycle findByIdWithPanelReviewSessions(Long id) {
        TypedQuery<ReviewCycle> query = em.createQuery(
                "SELECT rc FROM ReviewCycle rc LEFT JOIN FETCH rc.panelReviewSessions " +
                        "WHERE rc.id = :id", ReviewCycle.class);
        query.setParameter("id", id);
        List<ReviewCycle> results = query.getResultList();
        System.err.println("The results are:" + results.get(0));
        return results == null ? null : results.get(0);
    }

    public ReviewCycle getReviewCycleByContestTitleAndReviewCycleName(String contestTitleName, String reviewCycleName) {
        String jpql = "SELECT rc FROM ReviewCycle rc " +
                "JOIN rc.contest c " +
                "WHERE c.titleName = :contestTitleName AND rc.name = :reviewCycleName";
        TypedQuery<ReviewCycle> query = em.createQuery(jpql, ReviewCycle.class);
        query.setParameter("contestTitleName", contestTitleName);
        query.setParameter("reviewCycleName", reviewCycleName);
        return query.getSingleResult();
    }

}
