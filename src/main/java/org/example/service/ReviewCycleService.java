package org.example.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.dao.ReviewCycleDao;
import org.example.model.PanelReviewSession;
import org.example.model.ReviewCycle;
import org.example.util.JPAUtil;

import java.util.List;
import java.util.Optional;

public class ReviewCycleService {

    public ReviewCycle save(ReviewCycle reviewCycle) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            ReviewCycleDao reviewCycleDao = new ReviewCycleDao(em);
            return reviewCycleDao.save(reviewCycle);
        }
    }

    public ReviewCycle findById(Long id) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            Optional<ReviewCycle> contestOptional = new ReviewCycleDao(em).findById(id);

            if (contestOptional.isPresent()) {
                return contestOptional.get();
            } else {
                throw new RuntimeException("Contest not found for ID: " + id);
            }
        }
    }

    public ReviewCycle findByIdWithPanelReviewSeessions(Long id) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new ReviewCycleDao(em).findByIdWithPanelReviewSessions(id);
        }
    }

    public ReviewCycle saveWithPanelReviewSessions(long reviewCycleId, List<PanelReviewSession> panelReviewSessions) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
             transaction.begin();
            // Create Creator
            //check if user exists
            ReviewCycleDao reviewCycleDao = new ReviewCycleDao(em);
            ReviewCycle reviewCycle = reviewCycleDao.findByIdWithPanelReviewSessions(reviewCycleId);

            for (PanelReviewSession session:panelReviewSessions){
                reviewCycle.getPanelReviewMeets().add(session);
                session.setReviewCycle(reviewCycle);
            }

            // Save Contest
            ReviewCycle result = reviewCycleDao.save(reviewCycle);
            transaction.commit();
            return result;
        } catch (Exception e){
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
