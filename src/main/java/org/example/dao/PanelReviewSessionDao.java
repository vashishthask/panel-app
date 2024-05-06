package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.PanelReviewSession;
import org.example.model.ReviewCycle;

import java.util.List;

public class PanelReviewSessionDao {

    private final EntityManager entityManager;
    String jpql = "SELECT prm FROM PanelReviewSession prm JOIN prm.panelMembers pm WHERE pm.id = :panelMemberId AND prm NOT IN (SELECT e.panelReviewSession FROM Evaluation e WHERE e.panelMember.id = :panelMemberId)";


    public PanelReviewSessionDao(EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    public List<PanelReviewSession> findPendingSessionsForPanelMember(Long panelMemberId) {
        TypedQuery<PanelReviewSession> query = entityManager.createQuery(jpql, PanelReviewSession.class);
        query.setParameter("panelMemberId", panelMemberId);
        return query.getResultList();
    }

    public PanelReviewSession updatePanelReviewSession(PanelReviewSession panelReviewSession) {
        return entityManager.merge(panelReviewSession);
    }

    public List<PanelReviewSession> findAll() {
        TypedQuery<PanelReviewSession> query = entityManager.createQuery("SELECT p FROM PanelReviewSession p", PanelReviewSession.class);
        return query.getResultList();
    }

    public void deleteAll(){
        entityManager.createQuery("DELETE FROM PanelReviewSession").executeUpdate();
    }


    public void saveAll(List<PanelReviewSession> panelReviewSessions) {
        for (PanelReviewSession panelReviewSession : panelReviewSessions) {
            entityManager.persist(panelReviewSession);
        }
    }

    public PanelReviewSession save(PanelReviewSession panelReviewSession) {
        entityManager.persist(panelReviewSession);
        return panelReviewSession;
    }

    public List<PanelReviewSession> findByReviewCycle(ReviewCycle rc) {
        TypedQuery<PanelReviewSession> query = entityManager.createQuery(
            "SELECT prs FROM PanelReviewSession prs WHERE prs.reviewCycle = :rc", PanelReviewSession.class);
        query.setParameter("rc", rc);
        return query.getResultList();
    }

    public PanelReviewSession getPanelReviewSessionByIdWithEagerPanelMembers(long panelReviewSessionId) {
        String jpql = "SELECT prs FROM PanelReviewSession prs LEFT JOIN FETCH prs.panelMembers WHERE prs.id = :id";
        TypedQuery<PanelReviewSession> query = entityManager.createQuery(jpql, PanelReviewSession.class);
        query.setParameter("id", panelReviewSessionId);
        return query.getSingleResult();
    }
}
